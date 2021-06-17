# RoseDB's Official Java Driver
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/pw.mihou/Rose-Java-Driver/badge.png?style=flat)](https://maven-badges.herokuapp.com/maven-central/pw.mihou/Rose-Java-Driver)

The official Java driver for [RoseDB](https://github.com/ShindouMihou/RoseDB) with simple implementations; this covers all of the functions of RoseDB as of writing from updating values to deleting, adding values to filtering and aggregation of database and collections. It basically has everything.

## Important notes
Please use **JAVA 11** or higher since we do not support Java 8 anymore simply because it is too outdated.

## How to get started?
Assuming you have installed RoseDB onto a server or your own computer and also installed the driver onto your Java project (if you haven't please look at Installation section)
then we can move on to setting up the driver which is extremely simple and self-explanatory by itself.

To start, you have to create a `RoseDriver` class through:
```java
RoseDriver driver = new RoseBuilder().build("127.0.0.1", 5995, "authentication");
```

**Be sure to keep only one instance of the driver**

After creating your Driver instance, you may opt to use any of the methods below:

## Methods
These are all the methods you can use with the driver.
```java
/**
* Retrieving of data.
*/
CompletableFuture<RosePayload> get(String database, String collection, String identifier);

/**
* Aggregating of database and collection.
*/
CompletableFuture<AggregatedCollection> aggregate(String database, String collection);
CompletableFuture<AggregatedDatabase> aggregate(String database);

/**
* Adding of data (it will automatically create collection and database if it doesn't exist)
*/
CompletableFuture<RosePayload> add(String database, String collection, String identifier, JSONObject document);

/**
* Removing of databases, datas or collections.
*/
CompletableFuture<Boolean> remove(String database, String collection, String identifier, String key);
CompletableFuture<Boolean> remove(String database, String collection, String identifier);
CompletableFuture<Boolean> removeCollection(String database, String collection);
CompletableFuture<Boolean> removeDatabase(String database);

/**
* Updating of values.
*/
CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, String value);
CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, int value);
CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, long value);
CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, double value);
CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, boolean value);
CompletableFuture<RosePayload> update(String database, String collection, String identifier, String key, Object value);
CompletableFuture<RosePayload> update (String database, String collection, String identifier, Map<String, ?> map);

/**
* Filtering of a specific database's entire collections.
*/
CompletableFuture<AggregatedDatabase> filter(String database, String key, String value, FilterCasing casing);
CompletableFuture<AggregatedDatabase> filter(String database, String key, int value, NumberFilter filter);
CompletableFuture<AggregatedDatabase> filter(String database, String key, double value, NumberFilter filter);
CompletableFuture<AggregatedDatabase> filter(String database, String key, long value, NumberFilter filter);
CompletableFuture<AggregatedDatabase> filter(String database, String key, boolean value);
CompletableFuture<AggregatedDatabase> filter(String database, String key, T value);

/**
* Filtering of a specific database's collection.
*/
CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, String value, FilterCasing casing);
CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, int value, NumberFilter filter);
CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, double value, NumberFilter filter);
CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, long value, NumberFilter filter);
CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, boolean value);
CompletableFuture<AggregatedCollection> filter(String database, String collection, String key, T value);

/**
* Revert an item back to its previous state.
*/
CompletableFuture<RosePayload> revert(String database, String collection, String key);
```

## Number Filters
This is used to filter numbers using the `filter()` method.
```java
EQUALS, GREATER_THAN, LESS_THAN, GREATER_OR_EQUALS, LESS_OR_EQUALS;
```

## FilterCasing
This is used to filter strings using the `filter()` method.
```java
IGNORE_CASING, STRICT, IS_NOT_EQUALS_STRICT, IS_NOT_EQUALS_RELAXED;
```

## Rose Payload
Rose Payload is a custom class that holds the response from the server, it contains both the raw response, JSONObject and
a method to transform it into the specified class with the help of GSON (and also the kode response from the server).

An example of converting a response from Rose Payload to its class is:
```java
public class Item { 
	public String someKey;
	public String anotherKey;
}

driver.get("database", "collection", "identifier").thenApply(payload -> payload.as(Item.class))
.thenAccept(item -> System.out.println(item.someKey));
```

Please note that the value can be null, we did want to go Optional for this but we decided it wasn't best. To check
whether the response is null, you can do either:

The easy way is to check whether the kode is 1 which means the server returned a proper response.
```java
RosePayload payload = ...
if(payload.getKode() == 1){
    // ... do something with the payload.
}
```

The complicated part is to check the raw response itself whether it is null or empty.
```
RosePayload payload = ...
if(payload.getRaw() != null && !payload.getRaw().isEmpty() && !payload.getRaw().isBlank()){
... do something with the payload.
}
```

## Rose Item
Rose Item, unlike it's counterpart, will never return a null response since Rose Item is only acquireable through
aggregation which means it is guranteed to be a non-null response since the driver will never make a Rose Item without
a proper response from the server.

The methods between the two are the same with Rose Payload except for getKode() not being present.

## Exception Handling

It is important that you handle the exceptions that will come out from the CompletableFuture as well, if there is ever one.
You can use the CompletableFuture method `exceptionally(...)` to handle the exceptions.

An example of handling them is:
```java
driver.add("rose_db", "rose_collection", "identification", new JSONObject().put("someKey", "someValue"))
  .thenAccept(reply -> System.out.println(reply))
  .exceptionally(throwable -> {
    if(throwable != null)
      throwable.printStackTrace();
      
    return null;
    });
```

## Exceptions

There are only two exceptions that the driver will throw and that is: `FailedAuthorizationException`, `FileModificationException` and `FileDeletionException` which are all are explanatory.
* `FailedAuthorizationException` is used to indicate that the authorization code used on `RoseDriver` is invalid, might need to check `config.json` for the correct one.
* `FileModificationException` is used to indicate that an exception occurred with the most likely cause being a file being open while modification was occurring.
* `FileDeletionException` is used to indicate that an exception occurred with the most likely cause being a file being open while deletion was occurring.

## Listeners

You can add your own listeners that will intercept the messages sent from the server, though there isn't much you can do with it but you can go ahead and do that,
for example:

**Receiving Listener**
```java
import org.json.JSONObject;
import pw.mihou.rosedb.enums.Listening;
import pw.mihou.rosedb.listeners.interfaces.Listener;
import pw.mihou.rosedb.manager.ResponseManager;

public class onReceiveListener implements Listener {

    @Override
    public Listening type() {
        return Listening.RECEIVE;
    }

    @Override
    public void execute(JSONObject response) {
        System.out.println(response.toString);
    }
}

```

**Registering the listener**
```java
ListenerManager.addListener(new onReceiveListener());
```

## Installation

**Maven**
```xml
<dependency>
  <groupId>pw.mihou</groupId>
  <artifactId>Rose-Java-Driver</artifactId>
  <version>TAG</version>
</dependency>
```

**Gradle**

Step 1. Add the dependency
```gradle
	dependencies {
	        implementation 'pw.mihou:Rose-Java-Driver:Tag'
	}
```

## RoseDB installation
To install RoseDB to your server or computer, please follow the instructions on [RoseDB's repository](https://github.com/ShindouMihou/RoseDB)

## Credits
[TooTallNate/Java-Websocket](https://github.com/TooTallNate/Java-WebSocket) for the websocket client.

[org.json](https://mvnrepository.com/artifact/org.json/json/20210307) for the JSON parser.

## Maintainers
[Shindou Mihou](https://github.com/ShindouMihou)
