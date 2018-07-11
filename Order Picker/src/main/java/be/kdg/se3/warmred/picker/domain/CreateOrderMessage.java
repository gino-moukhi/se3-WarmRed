package be.kdg.se3.warmred.picker.domain;


import be.kdg.se3.warmred.picker.domain.dto.CreateOrderMessageDto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlType(propOrder = {"orderId", "customerId", "price", "items", "locationInfoList"})
@XmlRootElement
public class CreateOrderMessage implements Message{
    private int orderId;
    private int customerId;
    private int price;
    private Map<Integer, Integer> items = new HashMap<>();
    private List<LocationInfo> locationInfoList;

    public CreateOrderMessage() {
    }

    public CreateOrderMessage(int orderId, int customerId, int price, Map<Integer, Integer> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
        this.items = items;
        this.locationInfoList = new ArrayList<>();
    }

    public CreateOrderMessage(int orderId, int customerId, int price, Map<Integer, Integer> items, List<LocationInfo> locationInfoList) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
        this.items = items;
        this.locationInfoList = locationInfoList;
    }

    public CreateOrderMessage(CreateOrderMessageDto dto) {
        this.orderId = dto.getOrderId();
        this.customerId = dto.getCustomerId();
        this.price = dto.getPrice();
        this.items = dto.getItems();
        this.locationInfoList = new ArrayList<>();
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

    public List<LocationInfo> getLocationInfoList() {
        return locationInfoList;
    }

    public void setLocationInfoList(List<LocationInfo> locationInfoList) {
        this.locationInfoList = locationInfoList;
    }

    public void addLocationInfo(LocationInfo info) {
        locationInfoList.add(info);
    }

    public void removeLocationInfo(LocationInfo info) {
        locationInfoList.remove(info);
    }

    @Override
    public String toString() {
        return "CreateOrderMessage{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", price=" + price +
                ", items=" + items +
                ", locationInfo=" + locationInfoList +
                '}';
    }
}
