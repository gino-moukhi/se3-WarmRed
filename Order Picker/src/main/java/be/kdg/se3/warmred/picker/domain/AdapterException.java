package be.kdg.se3.warmred.picker.domain;

public class AdapterException extends Exception{
    public AdapterException(String message) {
        super(message);
    }

    public AdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
