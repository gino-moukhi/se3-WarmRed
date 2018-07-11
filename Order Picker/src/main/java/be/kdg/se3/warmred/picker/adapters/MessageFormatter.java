package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.Message;
import be.kdg.se3.warmred.picker.domain.dto.Dto;

public interface MessageFormatter {
    String format(Dto dto) throws FormatterException;

    Dto format(String xmlText) throws FormatterException;
}
