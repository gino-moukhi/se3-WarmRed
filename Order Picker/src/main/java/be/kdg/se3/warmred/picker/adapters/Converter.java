package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.LocationInfo;
import be.kdg.se3.warmred.picker.exceptions.ConverterException;

/**
 * Interface that is responsible for the conversion of the json String received from {@link be.kdg.se3.services.orderpicking.LocationServiceProxy}
 * to a {@link LocationInfo}
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface Converter {
    LocationInfo convertToLocationInfo(String jsonString) throws ConverterException;
}
