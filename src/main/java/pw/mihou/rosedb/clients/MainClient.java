package pw.mihou.rosedb.clients;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import pw.mihou.rosedb.listeners.ReceiveListener;
import pw.mihou.rosedb.manager.ListenerManager;

import java.net.URI;

public class MainClient extends WebSocketClient {

    public MainClient(URI serverUri){
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        ListenerManager.addListener(new ReceiveListener());
    }

    @Override
    public void onMessage(String s) {
        ListenerManager.pass(new JSONObject(s));
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        ListenerManager.removeListener(new ReceiveListener());
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
