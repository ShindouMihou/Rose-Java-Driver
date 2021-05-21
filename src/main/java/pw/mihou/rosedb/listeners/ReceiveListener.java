package pw.mihou.rosedb.listeners;

import org.json.JSONObject;
import pw.mihou.rosedb.enums.Listening;
import pw.mihou.rosedb.listeners.interfaces.Listener;
import pw.mihou.rosedb.manager.ResponseManager;

public class ReceiveListener implements Listener {

    @Override
    public Listening type() {
        return Listening.RECEIVE;
    }

    @Override
    public void execute(JSONObject response) {
        ResponseManager.responses.put(response.getString("replyTo"), response.toString());
    }
}
