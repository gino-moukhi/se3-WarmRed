package be.kdg.se3.warmred.picker.exceptions;

/**
 * Wrapper for anything that goes wrong during the formatting of a string or object
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class FormatterException extends Exception {
    public FormatterException(String message) {
        super(message);
    }

    public FormatterException(String message, Throwable cause) {
        super(message, cause);
    }
}
