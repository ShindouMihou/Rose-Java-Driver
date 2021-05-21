package pw.mihou.rosedb;

import pw.mihou.rosedb.impl.RoseDriverImpl;

import java.net.URI;
import java.net.URISyntaxException;

public class RoseBuilder {

    private final String format = "ws://%s:%d";

    public RoseDriver build(String address, int port, String authentication) throws URISyntaxException {
        return new RoseDriverImpl(new URI(String.format(format, address, port)), authentication);
    }

}
