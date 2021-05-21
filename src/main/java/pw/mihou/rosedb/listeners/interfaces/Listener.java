package pw.mihou.rosedb.listeners.interfaces;

import org.json.JSONObject;
import pw.mihou.rosedb.enums.Listening;

public interface Listener {

    Listening type();
    void execute(JSONObject response);

}
