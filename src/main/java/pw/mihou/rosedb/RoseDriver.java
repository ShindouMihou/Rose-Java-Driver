package pw.mihou.rosedb;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RoseDriver {

    /**
     * Retrieves data from the database and returns back
     * a JSONObject which you can use to retrieve your
     * preferred data.
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the name or identifier of the data.
     * @return JSONObject.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<JSONObject> get(String database, String collection, String identifier);

    /**
     * Adds an item to the database with the usage of JSONObject
     * since the database server requests for JSON and JSONObject has
     * all the methods you need to set it up, replacement for BSON Document if this was MongoDB.
     * @param database the database to place the data.
     * @param collection the collection to place the data.
     * @param identifier the identifier name of the data (which will be used to retrieve, update, remove).
     * @param document the document.
     * @return the same JSON Object but from the server (also with exceptions, if it fails).
     * @throws pw.mihou.rosedb.exceptions.FileModificationException if the server failed to add the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<JSONObject> add(String database, String collection, String identifier, JSONObject document);

    /**
     * Removes a key (and value) from an data (item).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be removed.
     * @return the updated data in the form of an JSONObject.
     * @throws pw.mihou.rosedb.exceptions.FileModificationException if the server failed to update the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<JSONObject> remove(String database, String collection, String identifier, String key);

    /**
     * Removes an item from the database.
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data to be removed.
     * @return whether the deletion was acknowledged
     * @throws pw.mihou.rosedb.exceptions.FileDeletionException if the server failed to remove the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<Boolean> remove(String database, String collection, String identifier);

    /**
     * Removes an entire collection (including its data).
     * @param database the database holding the data.
     * @param collection the collection holding the data to be removed.
     * @return whether the deletion was acknowledged
     * @throws pw.mihou.rosedb.exceptions.FileDeletionException if the server failed to remove the collection.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<Boolean> removeCollection(String database, String collection);

    /**
     * Removes an entire database (including its data).
     * @param database the database holding the data.
     * @return whether the deletion was acknowledged
     * @throws pw.mihou.rosedb.exceptions.FileDeletionException if the server failed to remove the database.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<Boolean> removeDatabase(String database);

    /**
     * Updates a key-value item (it will also add if it doesn't exist).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be paired with the value.
     * @param value the value that will be paired with the key.
     * @return the updated data in the form of an JSONObject.
     * @throws pw.mihou.rosedb.exceptions.FileModificationException if the server failed to update the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, String value);

    /**
     * Adds/updates/removes multiple key-value items to an data.
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param map a map of key-value pairs.
     * @return the updated data in the form of an JSONObject.
     * @throws pw.mihou.rosedb.exceptions.FileModificationException if the server failed to update the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<JSONObject> update (String database, String collection, String identifier, Map<String, String> map);

}
