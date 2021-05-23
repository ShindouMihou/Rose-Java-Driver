package pw.mihou.rosedb.manager;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import pw.mihou.rosedb.enums.Listening;
import pw.mihou.rosedb.listeners.interfaces.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private static final List<Listener> listeners = new ArrayList<>();

    public static void pass(JSONObject response){
        if(!response.isNull("kode") && !response.isNull("replyTo")){
            listeners.stream().filter(listener -> listener.type() == Listening.RECEIVE).forEach(listener -> listener.execute(response));
        }
    }

    public static void connect(ServerHandshake handshake){
        listeners.stream().filter(listener -> listener.type() == Listening.OPEN).forEach(listener -> listener.execute(new JSONObject()
        .put("httpStatus", handshake.getHttpStatus()).put("message", handshake.getHttpStatusMessage())));
    }

    public static void addListener(Listener listener){
        listeners.add(listener);
    }

    public static void removeListener(Listener listener){
        listeners.remove(listener);
    }

}
