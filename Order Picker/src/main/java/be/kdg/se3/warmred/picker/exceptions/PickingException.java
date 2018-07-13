package be.kdg.se3.warmred.picker.exceptions;

/**
 * Wrapper for anything that goes wrong during the optimization of the picking order
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class PickingException extends Exception {
    public PickingException(String message) {
        super(message);
    }

    public PickingException(String message, Throwable cause) {
        super(message, cause);
    }
}
