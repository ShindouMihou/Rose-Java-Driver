package pw.mihou.rosedb.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseManager {

    public static final Map<String, String> responses = new ConcurrentHashMap<>();

    public static String get(String key){
        String t = responses.get(key);
        responses.remove(key);
        RequestManager.requests.remove(key);
        return t;
    }

    public static boolean isNull(String key){
        return responses.get(key) == null;
    }

}
