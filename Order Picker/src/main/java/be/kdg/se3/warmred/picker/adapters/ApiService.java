package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.CommunicationException;

public interface ApiService {
    String getJsonResponse(int id) throws CommunicationException;
}
