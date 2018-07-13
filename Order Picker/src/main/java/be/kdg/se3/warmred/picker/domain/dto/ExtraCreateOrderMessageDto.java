package be.kdg.se3.warmred.picker.domain.dto;

import be.kdg.se3.warmred.picker.domain.CreateOrderMessage;
import be.kdg.se3.warmred.picker.domain.LocationInfo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.Map;

/**
 * A DTO class like {@link CreateOrderMessageDto} but this one contains {@link LocationInfo} and will be sent to the
 * outgoing queue (Internal: Logistics)
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
@XmlType(propOrder = {"orderId", "customerId", "price", "items", "locationInfoList"})
@XmlRootElement
public class ExtraCreateOrderMessageDto implements MessageDto {
    private int orderId;
    private int customerId;
    private int price;
    private Map<Integer, Integer> items;
    private List<LocationInfo> locationInfoList;

    public ExtraCreateOrderMessageDto() {
    }

    public ExtraCreateOrderMessageDto(CreateOrderMessage message) {
        this.orderId = message.getOrderId();
        this.customerId = message.getCustomerId();
        this.price = message.getPrice();
        this.items = message.getItems();
        this.locationInfoList = message.getLocationInfoList();
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

    public List<LocationInfo> getLocationInfoList() {
        return locationInfoList;
    }

    public void setLocationInfoList(List<LocationInfo> locationInfoList) {
        this.locationInfoList = locationInfoList;
    }
}
