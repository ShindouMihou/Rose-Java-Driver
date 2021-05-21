package pw.mihou.rosedb.exceptions;

public class FileDeletionException extends Exception {

    /**
     * File Deletion Exception is used to indicate that
     * an exception occurred with the most likely cause being a
     * file being open while deletion was occurring.
     * @param exception the exception message.
     */
    public FileDeletionException(String exception){
        super(exception);
    }

}
