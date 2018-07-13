package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.exceptions.CommunicationException;

/**
 * An interface that is designed for reading messages from a message queue (see {@link be.kdg.se3.warmred.picker.adapters.RabbitMQ} for an implementation)
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface MessageInputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void declareConsumer(MessageListener listener) throws CommunicationException;
}
