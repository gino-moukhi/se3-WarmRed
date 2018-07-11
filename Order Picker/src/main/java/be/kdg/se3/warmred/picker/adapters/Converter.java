package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.LocationInfo;

public interface Converter {
    LocationInfo convertToLocationInfo(String jsonString) throws ConverterException;
}
