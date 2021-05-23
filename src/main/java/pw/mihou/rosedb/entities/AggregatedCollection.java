package pw.mihou.rosedb.entities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AggregatedCollection {

    private final Map<String, JSONObject> data = new HashMap<>();
    private final String name;

    public AggregatedCollection(String name, JSONObject o){
        this.name = name;
        o.keySet().forEach(s -> data.put(s, new JSONObject(o.getString(s))));
    }

    /**
     * Returns the collection's name.
     * @return the collection name.
     */
    public String getCollectionName(){
        return name;
    }

    /**
     * Returns the data in the form of (identifier -> JSONObject).
     * The JSONObject holds all the key-values inside the identifier (or the object).
     * For example, user.rose has {"key":"value"}, the identifier would be "user" and
     * the object has the data within it: key which returns value.
     * @return Map<Identifier, Data>
     */
    public Map<String, JSONObject> getData(){
        return data;
    }

}
