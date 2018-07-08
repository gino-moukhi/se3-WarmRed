package be.kdg.se3.warmred.generator.domain;

import be.kdg.se3.warmred.generator.domain.dto.Dto;

public interface MessageOutputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void sendMessage(Message message) throws CommunicationException;

    void sendMessage(Dto dto) throws CommunicationException;

    void sendBasicMessage(String body) throws CommunicationException;
}
