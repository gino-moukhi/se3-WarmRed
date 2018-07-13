package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.exceptions.CommunicationException;

/**
 * Interface that is responsible for communicating with the {@link be.kdg.se3.services.orderpicking.LocationServiceProxy}
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface ApiService {
    String getJsonResponse(int id) throws CommunicationException;
}
