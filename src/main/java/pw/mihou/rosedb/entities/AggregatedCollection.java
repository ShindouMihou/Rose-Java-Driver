package pw.mihou.rosedb.entities;

import org.json.JSONObject;
import pw.mihou.rosedb.enums.FilterCasing;
import pw.mihou.rosedb.enums.NumberFilter;
import pw.mihou.rosedb.payloads.RoseItem;

import java.util.HashMap;
import java.util.Map;

public class AggregatedCollection {

    private final Map<String, RoseItem> data = new HashMap<>();
    private final String name;

    public AggregatedCollection(String name, JSONObject o){
        this.name = name;
        o.keySet().forEach(s -> data.put(s, new RoseItem(o.getString(s))));
    }

    public AggregatedCollection(String name, JSONObject o, String key, String value, FilterCasing casing){
        this.name = name;
        o.keySet().forEach(s -> {
            RoseItem y = new RoseItem(o.getString(s));
            JSONObject x = y.asJSONObject();
            if(!x.isNull(key)) {
                if (casing == FilterCasing.IGNORE_CASING) {
                    if (value.equalsIgnoreCase(x.getString(key))) {
                        data.put(s, y);
                    }
                } else if(casing == FilterCasing.STRICT){
                    if (value.equals(x.getString(key))) {
                        data.put(s, y);
                    }
                } else if(casing == FilterCasing.IS_NOT_EQUALS_STRICT){
                    if (!value.equals(x.getString(key))) {
                        data.put(s, y);
                    }
                } else if(casing == FilterCasing.IS_NOT_EQUALS_RELAXED){
                    if (!value.equalsIgnoreCase(x.getString(key))) {
                        data.put(s, y);
                    }
                }
            }
        });
    }

    public AggregatedCollection(String name, JSONObject o, String key, int value, NumberFilter filter){
        this.name = name;
        o.keySet().forEach(s -> {
            RoseItem y = new RoseItem(o.getString(s));
            JSONObject x = y.asJSONObject();
            if(!x.isNull(key)) {
                if(filter == NumberFilter.EQUALS) {
                    if (x.getInt(key) == value) {
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.GREATER_THAN){
                    if(x.getInt(key) > value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.LESS_THAN){
                    if(x.getInt(key) < value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.GREATER_OR_EQUALS){
                    if(x.getInt(key) >= value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.LESS_OR_EQUALS){
                    if(x.getInt(key) <= value){
                        data.put(s, y);
                    }
                }
            }
        });
    }

    public AggregatedCollection(String name, JSONObject o, String key, long value, NumberFilter filter){
        this.name = name;
        o.keySet().forEach(s -> {
            RoseItem y = new RoseItem(o.getString(s));
            JSONObject x = y.asJSONObject();
            if(!x.isNull(key)) {
                if(filter == NumberFilter.EQUALS) {
                    if (x.getLong(key) == value) {
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.GREATER_THAN){
                    if(x.getLong(key) > value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.LESS_THAN){
                    if(x.getLong(key) < value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.GREATER_OR_EQUALS){
                    if(x.getLong(key) >= value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.LESS_OR_EQUALS){
                    if(x.getLong(key) <= value){
                        data.put(s, y);
                    }
                }
            }
        });
    }

    public AggregatedCollection(String name, JSONObject o, String key, double value, NumberFilter filter){
        this.name = name;
        o.keySet().forEach(s -> {
            RoseItem y = new RoseItem(o.getString(s));
            JSONObject x = y.asJSONObject();
            if(!x.isNull(key)) {
                if(filter == NumberFilter.EQUALS) {
                    if (x.getDouble(key) == value) {
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.GREATER_THAN){
                    if(x.getDouble(key) > value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.LESS_THAN){
                    if(x.getDouble(key) < value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.GREATER_OR_EQUALS){
                    if(x.getDouble(key) >= value){
                        data.put(s, y);
                    }
                } else if(filter == NumberFilter.LESS_OR_EQUALS){
                    if(x.getDouble(key) <= value){
                        data.put(s, y);
                    }
                }
            }
        });
    }

    public <T> AggregatedCollection(String name, JSONObject o, String key, T value){
        this.name = name;
        o.keySet().forEach(s -> {
            RoseItem y = new RoseItem(o.getString(s));
            JSONObject x = y.asJSONObject();
            if(!x.isNull(key)) {
                if (x.get(key) == (T) value) {
                    data.put(s, y);
                }
            }
        });
    }

    /**
     * Returns the collection's name.
     * @return the collection name.
     */
    public String getCollectionName(){
        return name;
    }

    /**
     * Returns the data in the form of a map of the identifier and the RoseItem
     * which can be converted into either a JSONObject or transformed into the desired
     * class (raw is also available).
     * @return a map containing the identifier and data.
     */
    public Map<String, RoseItem> getData(){
        return data;
    }

}
