package pw.mihou.rosedb.exceptions;

public class FileModificationException extends Exception {

    /**
     * File Deletion Exception is used to indicate that
     * an exception occurred with the most likely cause being a
     * file being open while modification was occurring.
     * @param exception the exception message.
     */
    public FileModificationException(String exception){
        super(exception);
    }

}