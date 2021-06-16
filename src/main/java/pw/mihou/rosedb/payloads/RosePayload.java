package pw.mihou.rosedb.payloads;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import pw.mihou.rosedb.utility.RoseUtility;

public class RosePayload {

    @SerializedName("response")
    private String response;
    @SerializedName("kode")
    private int kode = -1;

    /**
     * The raw JSON data response from the server.
     * @return this is the absolute raw response fom the server in the JSON format.
     */
    public String getRaw(){
        return response;
    }

    /**
     * Gets the kode (signal) from RoseDB.
     * @return the signal returned.
     */
    public int getKode(){
        return kode;
    }

    /**
     * Transforms the response into the legacy JSONObject
     * which allows you to individually handpick what you want.
     * @return a JSONObject representative.
     */
    public JSONObject asJSONObject(){
        return new JSONObject(response);
    }

    /**
     * Transforms the response into an object through the help
     * of GSON, please note that it will throw an exception
     * if the JSON and Object does not match.
     * @param toClass the class to transform to.
     * @param <T> the type of class.
     * @return the transformed class.
     */
    public <T> T as(Class<T> toClass){
        return RoseUtility.gson.fromJson(response, toClass);
    }


}
