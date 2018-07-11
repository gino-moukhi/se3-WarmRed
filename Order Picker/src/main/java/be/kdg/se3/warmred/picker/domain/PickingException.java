package be.kdg.se3.warmred.picker.domain;

public class PickingException extends Exception {
    public PickingException(String message) {
        super(message);
    }

    public PickingException(String message, Throwable cause) {
        super(message, cause);
    }
}
