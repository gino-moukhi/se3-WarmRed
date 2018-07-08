package be.kdg.se3.warmred.generator.adapters;

class FormatterException extends Exception {
    FormatterException(String message) {
        super(message);
    }

    FormatterException(String message, Throwable cause) {
        super(message, cause);
    }
}
