package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.CreateOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.Dto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private MessageInputService messageInputService;

    public MessageHandler(MessageInputService messageInputService) {
        this.messageInputService = messageInputService;
    }

    public void start() {
        try {
            messageInputService.initialize();
        } catch (CommunicationException e) {
            logger.error("Unable to properly start the adapter");
        }
    }

    public void stop() {
        try {
            messageInputService.shutdown();
        } catch (CommunicationException e) {
            logger.error("Unable to properly shut down the adapter");
        }
    }

    @Override
    public void onReceive(Dto dtoMessage) {
        if (dtoMessage instanceof CreateOrderMessageDto) {
            CreateOrderMessage createOrderMessage = new CreateOrderMessage((CreateOrderMessageDto) dtoMessage);
            logger.info("Ready to do stuff with CreateOrderMessage with id: " + createOrderMessage.getOrderId());
            //TODO implement
        } else if (dtoMessage instanceof CancelOrderMessageDto) {
            CancelOrderMessage cancelOrderMessage = new CancelOrderMessage((CancelOrderMessageDto) dtoMessage);
            logger.info("Ready to do stuff with CancelOrderMessage with id: " + cancelOrderMessage.getOrderId());
            //TODO implement
        } else {
            logger.warn("This statement should not be reached otherwise something went wrong while formatting");
        }
    }
}
