package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.services.orderpicking.LocationServiceProxy;
import be.kdg.se3.warmred.picker.exceptions.CommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The class that is responsible for the communication with the {@link LocationServiceProxy} (see interface {@link Converter})
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class LocationServiceApi implements ApiService {
    private final Logger logger = LoggerFactory.getLogger(LocationServiceApi.class);
    private String baseUrl = "www.services4se3.com/locationservice/";
    private final LocationServiceProxy locationServiceProxy;

    public LocationServiceApi() {
        locationServiceProxy = new LocationServiceProxy();
    }

    public LocationServiceApi(String baseUrl) {
        this.baseUrl = baseUrl;
        locationServiceProxy = new LocationServiceProxy();
    }

    @Override
    public String getJsonResponse(int id) throws CommunicationException {
        String jsonResponse;
        try {
            logger.info("Making a call to " + baseUrl + id);
            jsonResponse = locationServiceProxy.get(baseUrl + id);

            logger.info("response as json: " + jsonResponse);
            return jsonResponse;
        } catch (IOException e) {
            logger.error("Could not reach the LocationServiceProxy");
            throw new CommunicationException("Could not reach the LocationServiceProxy ", e);
        }
    }
}
