package pw.mihou.rosedb.manager;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.mihou.rosedb.enums.Listening;
import pw.mihou.rosedb.listeners.interfaces.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private static final List<Listener> listeners = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(ListenerManager.class);

    public static void pass(JSONObject response){
        if(!response.isNull("kode") && !response.isNull("replyTo")){
            log.debug("Received request from server: {}", response.toString());
            listeners.stream().filter(listener -> listener.type() == Listening.RECEIVE).forEach(listener -> listener.execute(response));
        }
    }

    public static void connect(ServerHandshake handshake){
        log.debug("Received a handshake from the server: {}, {}", handshake.getHttpStatus(), handshake.getHttpStatusMessage());
        listeners.stream().filter(listener -> listener.type() == Listening.OPEN).forEach(listener -> listener.execute(new JSONObject()
        .put("httpStatus", handshake.getHttpStatus()).put("message", handshake.getHttpStatusMessage())));
    }

    public static void close(int i, String s, boolean b){
        // I don't know much about what i, s, b are so I will leave that there.
        listeners.stream().filter(listener -> listener.type() == Listening.CLOSE)
        .forEach(listener -> listener.execute(new JSONObject().put("i", i).put("s", s).put("b", b)));
    }

    public static void addListener(Listener listener){
        listeners.add(listener);
    }

    public static void removeListener(Listener listener){
        listeners.remove(listener);
    }

}
