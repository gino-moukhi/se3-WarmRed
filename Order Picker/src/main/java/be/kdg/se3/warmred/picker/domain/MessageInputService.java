package be.kdg.se3.warmred.picker.domain;

public interface MessageInputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void declareConsumer(MessageListener listener) throws CommunicationException;
}
