package be.kdg.se3.warmred.generator.adapters;

/**
 * Wrapper for anything that goes wrong during the formatting of a string or object
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
class FormatterException extends Exception {
    FormatterException(String message) {
        super(message);
    }

    FormatterException(String message, Throwable cause) {
        super(message, cause);
    }
}
