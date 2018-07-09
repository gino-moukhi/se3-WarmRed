package be.kdg.se3.warmred.picker.domain.dto;

import be.kdg.se3.warmred.picker.domain.CancelOrderMessage;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CancelOrderMessageDto implements Dto {
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
