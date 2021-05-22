package pw.mihou.rosedb.impl;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;
import pw.mihou.rosedb.RoseDriver;
import pw.mihou.rosedb.clients.MainClient;
import pw.mihou.rosedb.exceptions.FileDeletionException;
import pw.mihou.rosedb.exceptions.FileModificationException;
import pw.mihou.rosedb.manager.ResponseManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RoseDriverImpl implements RoseDriver {

    private final String authentication;
    private final WebSocketClient client;

    public RoseDriverImpl(URI connection, String authentication){
        client = new MainClient(connection);
        this.authentication = authentication;
        client.connect();
    }

    @Override
    public CompletableFuture<JSONObject> get(String database, String collection, String identifier) {
        return send(new JSONObject().put("collection", collection)
                .put("identifier", identifier), "get", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
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
    public CompletableFuture<JSONObject> update(String database, String collection, String identifier, Map<String, String> map) {
        return send(new JSONObject().put("identifier", identifier).put("key", new ArrayList<>(map.keySet())).put("value", new ArrayList<>(map.values()))
                .put("collection", collection), "update", database)
                .thenApply(jsonObject -> new JSONObject(jsonObject.getString("response")));
    }

    private CompletableFuture<JSONObject> send(JSONObject request, String method, String database){
        String unique = UUID.randomUUID().toString();
        return CompletableFuture.runAsync(() -> {
            client.send(request.put("authorization", authentication)
                    .put("method", method).put("database", database).put("unique", unique).toString());

            int i = 0;
            // If you have any better way of getting responses, please edit.
            while(ResponseManager.isNull(unique) && i < 30){
                try { i++; Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
        }).thenApply(unused -> {
            JSONObject response = new JSONObject(ResponseManager.get(unique));
            if(response.getInt("kode") != 1) {
                throw method.equalsIgnoreCase("delete") || method.equalsIgnoreCase("drop") ?
                        new CompletionException(new FileDeletionException(response.getString("response")))
                        : new CompletionException(new FileModificationException(response.getString("response")));
            }

            return response;
        });
    }
}
