package be.kdg.se3.warmred.picker.exceptions;

/**
 * Wrapper for anything that goes wrong during the communication with an external system through an adapter
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class CommunicationException extends Exception {
    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
