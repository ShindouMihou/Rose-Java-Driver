package pw.mihou.rosedb.exceptions;

public class FailedAuthorizationException extends Exception {

    /**
     * This is thrown whenever the authorization token sent
     * with the request is invalid, check your config.json
     * for the proper authorization code if that happens,
     * if you think this exception was thrown as a bug then
     * please inform me @ Github.
     * @param message the message sent by the server.
     */
    public FailedAuthorizationException(String message){
        super(message);
    }

}
