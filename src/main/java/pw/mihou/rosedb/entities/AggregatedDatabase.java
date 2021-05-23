package pw.mihou.rosedb.entities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AggregatedDatabase {

    private final List<AggregatedCollection> collectionList = new ArrayList<>();
    private final String name;

    public AggregatedDatabase(String name, JSONObject o){
        this.name = name;
        o.keySet().forEach(s -> collectionList.add(new AggregatedCollection(s, o.getJSONObject(s))));
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
