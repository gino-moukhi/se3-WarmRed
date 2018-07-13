package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.domain.dto.MessageDto;
import be.kdg.se3.warmred.picker.exceptions.CommunicationException;

/**
 * An interface that is designed for writing messages to a message queue (see {@link be.kdg.se3.warmred.picker.adapters.RabbitMQ} for an implementation)
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface MessageOutputService {
    void initialize() throws CommunicationException;

    void shutdown() throws CommunicationException;

    void sendMessage(MessageDto messageDto) throws CommunicationException;

}
