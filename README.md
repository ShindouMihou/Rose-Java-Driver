# RoseDB's Official Java Driver
[![](https://jitpack.io/v/pw.mihou/Rose-Java-Driver.svg)](https://jitpack.io/#pw.mihou/Rose-Java-Driver)

The official Java driver for [RoseDB](https://github.com/ShindouMihou/RoseDB) with simple implementations; this covers all of the functions of RoseDB as of writing from updating values to deleting, adding values to filtering and aggregation of database and collections. It basically has everything.

## Important notes
Please use **JAVA 11** since we do not support Java 8 anymore simply because it is too outdated.

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
CompletableFuture<JSONObject> get(String database, String collection, String identifier);

/**
* Aggregating of database and collection.
*/
CompletableFuture<AggregatedCollection> aggregate(String database, String collection);
CompletableFuture<AggregatedDatabase> aggregate(String database);

/**
* Adding of data (it will automatically create collection and database if it doesn't exist)
*/
CompletableFuture<JSONObject> add(String database, String collection, String identifier, JSONObject document);

/**
* Removing of databases, datas or collections.
*/
CompletableFuture<JSONObject> remove(String database, String collection, String identifier, String key);
CompletableFuture<Boolean> remove(String database, String collection, String identifier);
CompletableFuture<Boolean> removeCollection(String database, String collection);
CompletableFuture<Boolean> removeDatabase(String database);

/**
* Updating of values.
*/
CompletableFuture<JSONObject> update(String database, String collection, String identifier, String key, String value);
CompletableFuture<JSONObject> update (String database, String collection, String identifier, Map<String, String> map);

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

There are only two exceptions that the driver will throw and that is: `FileModificationException` and `FileDeletionException` which both are explanatory.
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

You can install the driver to your Java project with the help of Jitpack over at [Jitpack](https://jitpack.io/#pw.mihou/Rose-Java-Driver/)
or in case you want to install with Maven or Gradle, here are the stuff copied from Jitpack.

**Maven**

Step 1. Add the JitPack repository to your build file 
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Step 2. Add the dependency
```xml
	<dependency>
	    <groupId>pw.mihou</groupId>
	    <artifactId>Rose-Java-Driver</artifactId>
	    <version>Tag</version>
	</dependency>
```

**Gradle**

Step 1. Add it in your root build.gradle at the end of repositories:
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency
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
