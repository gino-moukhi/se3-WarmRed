package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.dto.MessageDto;
import be.kdg.se3.warmred.picker.exceptions.FormatterException;

/**
 * The class that is responsible for the formatting of the messages that are going to be placed on the message broker
 * or those that are received from the message broker
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface MessageFormatter {
    String format(MessageDto messageDto) throws FormatterException;

    MessageDto format(String xmlText) throws FormatterException;
}
