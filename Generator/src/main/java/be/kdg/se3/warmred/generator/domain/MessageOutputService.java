package be.kdg.se3.warmred.generator.domain;

import be.kdg.se3.warmred.generator.domain.dto.Dto;

/**
 * An interface that is designed for writing messages to a message queue (see {@link be.kdg.se3.warmred.generator.adapters.RabbitMQ} for an implementation)
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface MessageOutputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void sendMessage(Dto dto) throws CommunicationException;

    void sendBasicMessage(String body) throws CommunicationException;
}
