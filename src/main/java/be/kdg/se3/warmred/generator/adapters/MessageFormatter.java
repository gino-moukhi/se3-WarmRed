package be.kdg.se3.warmred.generator.adapters;

import be.kdg.se3.warmred.generator.domain.Message;

public interface MessageFormatter {
    String format(Message message) throws FormatterException;
}
