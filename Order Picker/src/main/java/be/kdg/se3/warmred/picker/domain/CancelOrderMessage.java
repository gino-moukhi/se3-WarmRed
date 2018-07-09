package be.kdg.se3.warmred.picker.domain;

import be.kdg.se3.warmred.picker.domain.dto.CancelOrderMessageDto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CancelOrderMessage implements Message{
    private int orderId;

    public CancelOrderMessage() {
    }

    public CancelOrderMessage(int orderId) {
        this.orderId = orderId;
    }

    public CancelOrderMessage(CancelOrderMessageDto dto) {
        this.orderId = dto.getOrderId();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "CancelOrderMessage{" +
                "orderId=" + orderId +
                '}';
    }
}
