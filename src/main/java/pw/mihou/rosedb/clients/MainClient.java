package pw.mihou.rosedb.clients;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import pw.mihou.rosedb.listeners.ReceiveListener;
import pw.mihou.rosedb.manager.ListenerManager;

import java.net.URI;

public class MainClient extends WebSocketClient {

    public boolean isConnected = false;

    public MainClient(URI serverUri){
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        isConnected = true;
        ListenerManager.addListener(new ReceiveListener());
        ListenerManager.connect(serverHandshake);
    }

    @Override
    public void onMessage(String s) {
        ListenerManager.pass(new JSONObject(s));
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        ListenerManager.removeListener(new ReceiveListener());
        ListenerManager.close(i, s, b);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
