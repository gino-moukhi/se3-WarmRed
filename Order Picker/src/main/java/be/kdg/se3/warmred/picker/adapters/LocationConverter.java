package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.LocationInfo;
import be.kdg.se3.warmred.picker.exceptions.ConverterException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that is responsible for the conversion of the json String to the locationInfo (see interface {@link Converter})
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class LocationConverter implements Converter {
    private final Logger logger = LoggerFactory.getLogger(LocationConverter.class);
    private final Gson gson;

    public LocationConverter() {
        gson = new Gson();
    }

    @Override
    public LocationInfo convertToLocationInfo(String jsonString) throws ConverterException {
        LocationInfo result;
        try {
            logger.info("Converting location info json to location info object");
            result = gson.fromJson(jsonString, LocationInfo.class);
            if (result.getProductID() == 0) {
                throw new ConverterException("Json contains different structure than expected: " + result);
            }
            logger.info("Converted info: " + result.toString());
        } catch (Exception e) {
            throw new ConverterException("Something went wrong while converting json data to LocationInfo");
        }
        return result;
    }
}
