package be.kdg.se3.warmred.generator.adapters;

import be.kdg.se3.warmred.generator.domain.dto.Dto;

/**
 * The class that is responsible for the formatting of the messages that are going to be placed on the message broker
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface MessageFormatter {
    String format(Dto dto) throws FormatterException;
}
