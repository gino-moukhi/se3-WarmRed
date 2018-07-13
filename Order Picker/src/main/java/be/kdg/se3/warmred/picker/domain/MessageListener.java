package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.domain.dto.MessageDto;

/**
 * An interface that handles messages when they are received from the message queue
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public interface MessageListener {
    void onReceive(MessageDto messageDtoMessage);
}
