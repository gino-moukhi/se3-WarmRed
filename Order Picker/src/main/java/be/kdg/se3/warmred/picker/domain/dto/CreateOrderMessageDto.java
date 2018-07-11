package be.kdg.se3.warmred.picker.domain.dto;

import be.kdg.se3.warmred.picker.domain.CreateOrderMessage;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Map;

@XmlType(propOrder = {"orderId", "customerId", "price", "items"})
@XmlRootElement
public class CreateOrderMessageDto implements Dto {
    private int orderId;
    private int customerId;
    private int price;
    private Map<Integer, Integer> items;

    public CreateOrderMessageDto() {
    }

    public CreateOrderMessageDto(int orderId, int customerId, int price, Map<Integer, Integer> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
        this.items = items;
    }

    public CreateOrderMessageDto(CreateOrderMessage message) {
        this.orderId = message.getOrderId();
        this.customerId = message.getCustomerId();
        this.price = message.getPrice();
        this.items = message.getItems();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }

    public void addItem(int productId, int amount) {
        this.items.put(productId, amount);
    }

    public void removeItem(int productId) {
        this.items.remove(productId);
    }

    @Override
    public String toString() {
        return "CreateOrderMessageDto{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", price=" + price +
                ", items=" + items +
                '}';
    }
}
