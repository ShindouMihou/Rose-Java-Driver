package pw.mihou.rosedb.impl;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.mihou.rosedb.RoseDriver;
import pw.mihou.rosedb.clients.MainClient;
import pw.mihou.rosedb.entities.AggregatedCollection;
import pw.mihou.rosedb.entities.AggregatedDatabase;
import pw.mihou.rosedb.enums.FilterCasing;
import pw.mihou.rosedb.enums.NumberFilter;
import pw.mihou.rosedb.exceptions.FailedAuthorizationException;
import pw.mihou.rosedb.exceptions.FailedConnectionException;
import pw.mihou.rosedb.exceptions.FileDeletionException;
import pw.mihou.rosedb.exceptions.FileModificationException;
import pw.mihou.rosedb.io.Scheduler;
import pw.mihou.rosedb.manager.RequestManager;
import pw.mihou.rosedb.manager.ResponseManager;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

public class RoseDriverImpl implements RoseDriver {

    private final String authentication;
    private final WebSocketClient client;
    private final int timeout;
    private final TimeUnit unit;
    private boolean shutdown = false;
    private static final Logger log = LoggerFactory.getLogger(RoseDriver.class);

    public RoseDriverImpl(URI connection, String authentication, int timeout, TimeUnit unit){
        client = new MainClient(connection);
        this.authentication = authentication;
        this.timeout = timeout;
        this.unit = unit;
        client.addHeader("authorization", authentication);
        client.connect();

        Scheduler.schedule(() -> {
            if(!((MainClient) client).isConnected) {
                log.error("Failed to connect to {}, please check your debug console on RoseDB for more information.", connection.toString());
                shutdown = true;
            }
        }, timeout, unit);
    }

    public RoseDriverImpl(URI connection, String authentication, boolean blocking, int timeout, TimeUnit unit) throws FailedConnectionException {
        client = new MainClient(connection);
        this.authentication = authentication;
        this.timeout = timeout;
        this.unit = unit;
        client.addHeader("authorization", authentication);
        if(blocking) {
            try {
                log.debug("Attempting to connect to {}...", connection.toString());
                if (!client.connectBlocking(timeout, unit)) {
                    log.error("Failed to connect to {}, please check your debug console on RoseDB for more information.", connection.toString());
                    throw new FailedConnectionException("Failed to connect to " + connection.toString() + ", please check your debug console for RoseDB.");
                }

                if(!((MainClient) client).isConnected)
                    throw new FailedConnectionException("Failed to connect to " + connection.toString() + ", please check your debug console for RoseDB.");

            } catch (InterruptedException e) {
                log.error("Failed to connect to {}: {}", connection.toString(), e.getMessage());
                throw new FailedConnectionException("Failed to connect to " + connection.toString() + ": " + e.getMessage());
            }
        } else {
            client.connect();
        }
    }

    @Override
    public CompletableFuture<JSONObject> get(String database, String collection, String identifier) {
        return send(new JSONObject().put("collection", collection)
                .put("identifier", identifier), "get", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<AggregatedDatabase> aggregate(String database) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database)));
    }

    @Override
    public CompletableFuture<AggregatedCollection> aggregate(String database, String collection) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection)));
    }

    @Override
    public CompletableFuture<AggregatedDatabase> filter(String database, String key, String value, FilterCasing casing) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database), key, value, casing));
    }

    @Override
    public CompletableFuture<AggregatedDatabase> filter(String database, String key, long value, NumberFilter filter) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database), key, value, filter));
    }

    @Override
    public CompletableFuture<AggregatedDatabase> filter(String database, String key, double value, NumberFilter filter) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database), key, value, filter));
    }

    @Override
    public <T> CompletableFuture<AggregatedDatabase> filter(String database, String key, T value) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database), key, value));
    }

    @Override
    public CompletableFuture<AggregatedDatabase> filter(String database, String key, int value, NumberFilter filter) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database), key, value, filter));
    }

    @Override
    public CompletableFuture<AggregatedDatabase> filter(String database, String key, boolean value) {
        return send(new JSONObject().put("database", database))
                .thenApply(o -> new AggregatedDatabase(database, o.getJSONObject(database), key, value));
    }

    @Override
    public CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, String value, FilterCasing casing) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection), key, value, casing));
    }

    @Override
    public CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, long value, NumberFilter filter) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection), key, value, filter));
    }

    @Override
    public CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, double value, NumberFilter filter) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection), key, value, filter));
    }

    @Override
    public <T> CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, T value) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection), key, value));
    }

    @Override
    public CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, int value, NumberFilter filter) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection), key, value, filter));
    }

    @Override
    public CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, boolean value) {
        return send(new JSONObject().put("database", database).put("collection", collection))
                .thenApply(o -> new AggregatedCollection(collection, o.getJSONObject(collection), key, value));
    }

    @Override
    public CompletableFuture<JSONObject> add(String database, String collection, String identifier, JSONObject document) {
        return send(new JSONObject().put("collection", collection)
                .put("identifier", identifier).put("value", document.toString()), "add", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> remove(String database, String collection, String identifier, String key) {
        return send(new JSONObject().put("collection", collection).put("identifier", identifier)
        .put("key", key), "delete", database).thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> remove(String database, String collection, String identifier, Collection<String> keys) {
        return send(new JSONObject().put("collection", collection).put("identifier", identifier)
                .put("key", keys), "delete", database).thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<Boolean> remove(String database, String collection, String identifier) {
        return send(new JSONObject().put("collection", collection).put("identifier", identifier), "delete", database)
                .thenApply(jsonObject -> jsonObject.getInt("kode") == 1);
    }

    @Override
    public CompletableFuture<Boolean> removeCollection(String database, String collection) {
        return send(new JSONObject().put("collection", collection), "drop", database).thenApply(jsonObject -> jsonObject.getInt("kode") == 1);
    }

    @Override
    public CompletableFuture<Boolean> removeDatabase(String database) {
        return send(new JSONObject(), "drop", database).thenApply(jsonObject -> jsonObject.getInt("kode") == 1);
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, String value) {
        return send(new JSONObject().put("identifier", identifier).put("key", key).put("value", value)
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, int value) {
        return send(new JSONObject().put("identifier", identifier).put("key", key).put("value", value)
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, boolean value) {
        return send(new JSONObject().put("identifier", identifier).put("key", key).put("value", value)
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, double value) {
        return send(new JSONObject().put("identifier", identifier).put("key", key).put("value", value)
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, long value) {
        return send(new JSONObject().put("identifier", identifier).put("key", key).put("value", value)
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, Object value) {
        return send(new JSONObject().put("identifier", identifier).put("key", key).put("value", value)
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    @Override
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, Map<String, ?> map) {
        return send(new JSONObject().put("identifier", identifier).put("key", new ArrayList<>(map.keySet()))
                .put("value", new ArrayList<>(map.values()))
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    private CompletableFuture<JSONObject> send(JSONObject request){
        if(!shutdown) {
            String unique = UUID.randomUUID().toString();
            return CompletableFuture.runAsync(() -> {
                client.send(request.put("method", "aggregate").put("unique", unique).toString());
                RequestManager.requests.add(unique);

                int i = 0;
                // If you have any better way of getting responses, please edit.
                while (ResponseManager.isNull(unique) && i < 30) {
                    try {
                        i++;
                        Thread.sleep(5);
                    } catch (InterruptedException ignored) {
                    }
                }
            }).thenApply(unused -> {
                if(ResponseManager.isNull(unique))
                    throw new CompletionException(new FailedAuthorizationException("Please validate: correct authorization code or unique value on request."));

                JSONObject response = new JSONObject(ResponseManager.get(unique));
                RequestManager.requests.remove(unique);
                if (response.getInt("kode") != 1) {
                    throw new CompletionException(new FailedAuthorizationException(response.getString("response")));
                }

                return response;
            });
        }
        return CompletableFuture.supplyAsync(() -> new JSONObject().put("kode", -1));
    }

    private CompletableFuture<JSONObject> send(JSONObject request, String method, String database){
        if(!shutdown) {
            String unique = UUID.randomUUID().toString();
            return CompletableFuture.runAsync(() -> {
                client.send(request
                        .put("method", method).put("database", database).put("unique", unique).toString());
                RequestManager.requests.add(unique);

                int i = 0;
                // If you have any better way of getting responses, please edit.
                while (ResponseManager.isNull(unique) && i < 30) {
                    try {
                        i++;
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).thenApply(unused -> {
                // There is only one response when the value is null.
                if(ResponseManager.isNull(unique))
                    throw new CompletionException(new FailedAuthorizationException("Please validate: correct authorization code or unique value on request."));

                JSONObject response = new JSONObject(ResponseManager.get(unique));
                RequestManager.requests.remove(unique);
                if (response.getInt("kode") != 1) {
                    throw response.getString("response").equalsIgnoreCase("Please validate: correct authorization code or unique value on request.")
                            ? new CompletionException(new FailedAuthorizationException(response.getString("response")))
                            : (method.equalsIgnoreCase("delete") || method.equalsIgnoreCase("drop")
                            ? new CompletionException(new FileDeletionException(response.getString("response")))
                            : new CompletionException(new FileModificationException(response.getString("response"))));
                }

                return response;
            });
        }
        return CompletableFuture.supplyAsync(() -> new JSONObject().put("kode", -1));
    }

    @Override
    public void shutdown(){
        shutdown("The client requested a shutdown");
    }

    @Override
    public void shutdown(String message){
        shutdown = true;
        if (!RequestManager.requests.isEmpty()) {
            int i = 0;
            while (!RequestManager.requests.isEmpty() && i < unit.toSeconds(timeout)) {
                try {
                    i++;
                    log.info("Waiting for requests: [{}] to complete...", String.join(", ", RequestManager.requests));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        log.info("The client is now closing down...");
        client.close(1000, message);
    }

    @Override
    public CompletableFuture<Void> shutdownAsync() {
        return CompletableFuture.runAsync(() -> shutdown("The client requested a shutdown."));
    }

    @Override
    public CompletableFuture<Void> shutdownAsync(String message) {
        return CompletableFuture.runAsync(() -> shutdown(message));
    }

    @Override
    public void forceShutdown() {
        forceShutdown("The client requested a shutdown.");
    }

    @Override
    public void forceShutdown(String message) {
        shutdown = true;
        log.debug("The client is now closing down...");
        client.close(1000, message);
    }
}
