package pw.mihou.rosedb;

import org.json.JSONObject;
import pw.mihou.rosedb.entities.AggregatedCollection;
import pw.mihou.rosedb.entities.AggregatedDatabase;
import pw.mihou.rosedb.enums.FilterCasing;
import pw.mihou.rosedb.enums.NumberFilter;
import pw.mihou.rosedb.exceptions.FailedAuthorizationException;
import pw.mihou.rosedb.exceptions.FileModificationException;
import pw.mihou.rosedb.payloads.RosePayload;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RoseDriver {

    /**
     * Retrieves data from the database and returns back
     * a RosePayload which you can use to retrieve your
     * preferred data.
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the name or identifier of the data.
     * @return RosePayload.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> get(String database, String collection, String identifier);

    /**
     * Retrieves all data from the database including collections.
     * @param database the database holding the data.
     * @return AggregatedCollection.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<AggregatedDatabase> aggregate(String database);

    /**
     * Retrieves all data from a specific collecton in a specific database.
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @return AggregatedDatabase.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<AggregatedCollection> aggregate(String database, String collection);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param casing whether to ignore casing or not.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedDatabase> filter(String database, String key, String value, FilterCasing casing);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param filter The filter to use.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedDatabase> filter(String database, String key, long value, NumberFilter filter);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param filter The filter to use.
     * @return Filtered AggregatedDatabase
     */
    CompletableFuture<AggregatedDatabase> filter(String database, String key, double value, NumberFilter filter);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param <T> the object to return.
     * @return Filtered AggregatedDatabase
     */
    <T> CompletableFuture<AggregatedDatabase> filter(String database, String key, T value);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param filter The filter to use.
     * @return Filtered AggregatedDatabase
     */
    CompletableFuture<AggregatedDatabase> filter(String database, String key, int value, NumberFilter filter);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @return Filtered AggregatedDatabase
     */
    CompletableFuture<AggregatedDatabase> filter(String database, String key, boolean value);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param collection the collection to find.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param casing whether to ignore casing or not.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, String value, FilterCasing casing);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param collection the collection to find.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param filter The filter to use.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, long value, NumberFilter filter);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param collection the collection to find.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param filter The filter to use.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, double value, NumberFilter filter);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param collection the collection to find.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param <T> the object to filter.
     * @return Filtered Aggregated Collection.
     */
    <T> CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, T value);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param collection the collection to find.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @param filter The filter to use.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, int value, NumberFilter filter);

    /**
     * Finds a list of all objects that has the key and value inside a database and collection.
     * This utilizes aggregation methods.
     * @param database the database to find on.
     * @param collection the collection to find.
     * @param key the key to check.
     * @param value the value to search for on the objects.
     * @return Filtered Aggregated Collection.
     */
    CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, boolean value);

    /**
     * Adds an item to the database.
     * @param database the database to place the data.
     * @param collection the collection to place the data.
     * @param identifier the identifier name of the data (which will be used to retrieve, update, remove).
     * @param document the document.
     * @return the same JSON Object but from the server (also with exceptions, if it fails).
     * @throws pw.mihou.rosedb.exceptions.FileModificationException if the server failed to add the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> add(String database, String collection, String identifier, JSONObject document);

    /**
     * Adds an item to the database with the usage of RosePayload.
     * @param database the database to place the data.
     * @param collection the collection to place the data.
     * @param identifier the identifier name of the data (which will be used to retrieve, update, remove).
     * @param document the object to add..
     * @return the same JSON Object but from the server (also with exceptions, if it fails).
     * @throws pw.mihou.rosedb.exceptions.FileModificationException if the server failed to add the item.
     * @throws pw.mihou.rosedb.exceptions.FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    <T> CompletableFuture<RosePayload> add(String database, String collection, String identifier, T document);


    /**
     * Removes a key (and value) from an data (item).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be removed.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> remove(String database, String collection, String identifier, String key);
    /**
     * Removes a key (and value) from an data (item).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param keys the keys that will be removed.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> remove(String database, String collection, String identifier, Collection<String> keys);


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
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, String value);


    /**
     * Updates a key-value item (it will also add if it doesn't exist).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be paired with the value.
     * @param value the value that will be paired with the key.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, int value);

    /**
     * Updates a key-value item (it will also add if it doesn't exist).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be paired with the value.
     * @param value the value that will be paired with the key.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, boolean value);

    /**
     * Updates a key-value item (it will also add if it doesn't exist).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be paired with the value.
     * @param value the value that will be paired with the key.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, double value);

    /**
     * Updates a key-value item (it will also add if it doesn't exist).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be paired with the value.
     * @param value the value that will be paired with the key.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, long value);

    /**
     * Updates a key-value item (it will also add if it doesn't exist).
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param key the key that will be paired with the value.
     * @param value the value that will be paired with the key.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, Object value);

    /**
     * Adds/updates/removes multiple key-value items to an data.
     * @param database the database holding the data.
     * @param collection the collection holding the data.
     * @param identifier the identifier of the data.
     * @param map a map of key-value pairs.
     * @return the updated data in the form of an RosePayload.
     * @throws FileModificationException if the server failed to update the item.
     * @throws FailedAuthorizationException throws authorization exception if authentication code is invalid.
     */
    CompletableFuture<RosePayload> update (String database, String collection, String identifier, Map<String, ?> map);

    /**
     * Reverts an item to its previous form.
     * @param database the database where the item is located.
     * @param collection the collection where the item is locate.d
     * @param identifier the identifier/name of the item.
     * @return a payload that contains the response and kode.
     */
    CompletableFuture<RosePayload> revert(String database, String collection, String identifier);
    /**
     * Performs a graceful close on the client's connection with the server.
     * It waits for all the currently running requests to finish before closing the client.
     * WARNING: This will block the thread.
     *
     * This causes all requests afterwards to fail and could potentially cause
     * NPES (NullPointerExceptions), though you should be able to close the
     * client without having to call this since the client automatically closes it.
     */
    void shutdown();

    /**
     * Performs a graceful close on the client's connection with the server.
     * It waits for all the currently running requests to finish before closing the client.
     * WARNING: This will block the thread.
     *
     * This causes all requests afterwards to fail and could potentially cause
     * NPES (NullPointerExceptions), though you should be able to close the
     * client without having to call this since the client automatically closes it.
     * @param message The message to send to the server.
     */
    void shutdown(String message);

    /**
     * Performs a graceful close on the client's connection with the server.
     * It waits for all the currently running requests to finish before closing the client.
     *
     * This causes all requests afterwards to fail and could potentially cause
     * NPES (NullPointerExceptions), though you should be able to close the
     * client without having to call this since the client automatically closes it.
     * @return CompletableFuture to handle.
     */
    CompletableFuture<Void> shutdownAsync();

    /**
     * Performs a graceful close on the client's connection with the server.
     * It waits for all the currently running requests to finish before closing the client.
     *
     * This causes all requests afterwards to fail and could potentially cause
     * NPES (NullPointerExceptions), though you should be able to close the
     * client without having to call this since the client automatically closes it.
     * @param message The message to send to the server.
     * @return CompletableFuture to handle.
     */
    CompletableFuture<Void> shutdownAsync(String message);

    /**
     * Performs a forced close on the client's connection with the server.
     * It waits for all the currently running requests to finish before closing the client.
     *
     * This causes all requests afterwards to fail and could potentially cause
     * NPES (NullPointerExceptions), though you should be able to close the
     * client without having to call this since the client automatically closes it.
     */
    void forceShutdown();

    /**
     * Performs a forced close on the client's connection with the server.
     * It waits for all the currently running requests to finish before closing the client.
     *
     * This causes all requests afterwards to fail and could potentially cause
     * NPES (NullPointerExceptions), though you should be able to close the
     * client without having to call this since the client automatically closes it.
     * @param message The message to send to the server.
     */
    void forceShutdown(String message);
}
