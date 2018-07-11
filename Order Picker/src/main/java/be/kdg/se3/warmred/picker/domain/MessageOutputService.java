package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.domain.dto.Dto;

public interface MessageOutputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void sendMessage(Dto dto) throws CommunicationException;

    void sendBasicMessage(String body) throws CommunicationException;
}
