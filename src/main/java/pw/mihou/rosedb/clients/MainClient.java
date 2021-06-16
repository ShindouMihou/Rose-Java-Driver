package pw.mihou.rosedb.clients;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.mihou.rosedb.exceptions.FailedConnectionException;
import pw.mihou.rosedb.io.Scheduler;
import pw.mihou.rosedb.listeners.ReceiveListener;
import pw.mihou.rosedb.manager.ListenerManager;

import java.net.URI;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClient extends WebSocketClient {

    public static final Logger log = LoggerFactory.getLogger(MainClient.class);
    public final URI uri;
    public boolean isConnected = false;
    public String error;
    private boolean reconnecting = false;

    public MainClient(URI serverUri) {
        super(serverUri);
        this.uri = serverUri;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        isConnected = true;
        ListenerManager.addListener(new ReceiveListener());
        ListenerManager.connect(serverHandshake);
    }

    public CompletableFuture<Boolean> connect(int timeout, TimeUnit unit, boolean blocking) throws FailedConnectionException {
        if (blocking) {
            try {
                log.debug("Attempting to connect to {}...", uri.toString());
                if (!connectBlocking(timeout, unit) && this.error != null) {
                    log.error("Failed to connect to {}: {}", uri.toString(), error);
                    throw new FailedConnectionException("Failed to connect to " + uri.toString() + ": " + error);
                }

                if (!this.isConnected)
                    throw new FailedConnectionException("Failed to connect to " + uri.toString() + ": " + error);

                return CompletableFuture.supplyAsync(() -> true);
            } catch (InterruptedException e) {
                log.error("Failed to connect to {}: {}", uri.toString(), e.getMessage());
                throw new FailedConnectionException("Failed to connect to " + uri.toString() + ": " + e.getMessage());
            }
        } else {
            connect();
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return Scheduler.getScheduler().schedule(() -> {
                        if (!this.isConnected && this.error != null) {
                            log.error("Failed to connect to {}: {}", uri.toString(), this.error);
                            return false;
                        }

                        return true;
                    }, timeout, unit).get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Failed to connect to {}: {}", uri.toString(), e.getMessage());
                    throw new CompletionException(new FailedConnectionException("Failed to connect to " + uri.toString() + ": " + e.getMessage()));
                }
            }).exceptionally(Objects::isNull);
        }
    }

    @Override
    public void onMessage(String s) {
        ListenerManager.pass(new JSONObject(s));
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        isConnected = false;
        ListenerManager.close(i, s, b);

        if (i == 4001) {
            error = s;
            return;
        }

        if (i == 1000)
            return;

        if (!reconnecting) {
            Scheduler.executorService.submit(() -> bucketReconnect(new AtomicInteger(0)));
        }
    }

    public void bucketReconnect(AtomicInteger i) {
        reconnecting = true;
        log.info("Client has disconnected from server, attempting to reconnect in {} seconds", i.addAndGet(1));
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(i.get()));
            if (!reconnectBlocking()) {
                bucketReconnect(i);
                return;
            }

            isConnected = true;
            reconnecting = false;
            log.info("Client has reconnected to server successfully.");
        } catch (InterruptedException e) {
            log.error("Failed to connect to {}: {}", uri.toString(), e.getMessage());
            bucketReconnect(i);
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
