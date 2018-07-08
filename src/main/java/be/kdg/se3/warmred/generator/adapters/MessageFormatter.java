package be.kdg.se3.warmred.generator.adapters;

import be.kdg.se3.warmred.generator.domain.Message;
import be.kdg.se3.warmred.generator.domain.dto.Dto;

public interface MessageFormatter {
    String format(Message message) throws FormatterException;
    String format(Dto dto) throws FormatterException;
}
