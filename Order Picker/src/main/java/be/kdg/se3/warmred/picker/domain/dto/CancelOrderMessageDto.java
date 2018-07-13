package be.kdg.se3.warmred.picker.domain.dto;

import be.kdg.se3.warmred.picker.domain.CancelOrderMessage;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A DTO class that is suitable for leaving the application and being put on the message broker.
 * This class will be used if an order cancellation is sent/received from the message broker.
 * See {@link CancelOrderMessage} for the class that is not ready for message broker utilisation.
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
@XmlRootElement
public class CancelOrderMessageDto implements MessageDto {
    private int orderId;

    public CancelOrderMessageDto() {
    }

    public CancelOrderMessageDto(int orderId) {
        this.orderId = orderId;
    }

    public CancelOrderMessageDto(CancelOrderMessage message) {
        this.orderId = message.getOrderId();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
