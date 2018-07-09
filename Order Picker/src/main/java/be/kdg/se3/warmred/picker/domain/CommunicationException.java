package be.kdg.se3.warmred.picker.domain;

public class CommunicationException extends Exception {
    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
