package pw.mihou.rosedb.entities;

import org.json.JSONObject;
import pw.mihou.rosedb.enums.FilterCasing;
import pw.mihou.rosedb.enums.NumberFilter;

import java.util.ArrayList;
import java.util.List;

public class AggregatedDatabase {

    private final List<AggregatedCollection> collectionList = new ArrayList<>();
    private final String name;

    public AggregatedDatabase(String name, JSONObject o){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s))));
    }

    public AggregatedDatabase(String name, JSONObject o, String key, String value, FilterCasing casing){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s), key, value, casing)));
    }

    public AggregatedDatabase(String name, JSONObject o, String key, int value, NumberFilter filter){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s), key, value, filter)));
    }

    public AggregatedDatabase(String name, JSONObject o, String key, long value, NumberFilter filter){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s), key, value, filter)));
    }

    public AggregatedDatabase(String name, JSONObject o, String key, double value, NumberFilter filter){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s), key, value, filter)));
    }

    public AggregatedDatabase(String name, JSONObject o, String key, boolean value){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s), key, value)));
    }

    public <T> AggregatedDatabase(String name, JSONObject o, String key, T value){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s), key, value)));
    }

    /**
     * Returns the list of collections that were inside the database (including their data).
     * @return the list of collections with their data.
     */
    public List<AggregatedCollection> getCollectionList(){
        return collectionList;
    }

    /**
     * Returns the database's name.
     * @return the database name.
     */
    public String getDatabaseName(){
        return name;
    }


}
