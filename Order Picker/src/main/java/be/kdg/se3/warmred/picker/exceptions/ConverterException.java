package be.kdg.se3.warmred.picker.exceptions;

/**
 * Wrapper for anything that goes wrong with the conversion of objects
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class ConverterException extends Exception {
    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
