package pw.mihou.rosedb;

import pw.mihou.rosedb.exceptions.FailedConnectionException;
import pw.mihou.rosedb.impl.RoseDriverImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class RoseBuilder {

    private final String format = "ws://%s:%d";
    private int timeout = 5;
    private TimeUnit unit = TimeUnit.SECONDS;

    public RoseBuilder setTimeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    public RoseBuilder setTimeUnit(TimeUnit unit){
        this.unit = unit;
        return this;
    }

    public RoseDriver buildAsync(String address, int port, String authentication) throws URISyntaxException {
        return new RoseDriverImpl(new URI(String.format(format, address, port)), authentication, timeout, unit);
    }

    public RoseDriver build(String address, int port, String authentication) throws URISyntaxException, FailedConnectionException {
        return new RoseDriverImpl(new URI(String.format(format, address, port)), authentication, true, timeout, unit);
    }

}
