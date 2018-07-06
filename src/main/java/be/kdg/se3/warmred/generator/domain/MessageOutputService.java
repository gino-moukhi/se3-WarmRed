package be.kdg.se3.warmred.generator.domain;

public interface MessageOutputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void sendMessage(Message message) throws CommunicationException;

    void sendBasicMessage(String body) throws CommunicationException;
}
