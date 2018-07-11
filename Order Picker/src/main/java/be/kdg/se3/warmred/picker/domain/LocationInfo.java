package be.kdg.se3.warmred.picker.domain;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType(propOrder = {"productID", "storageRoom", "hallway", "rack"})
public class LocationInfo implements Comparable<LocationInfo>{
    private int productID;
    private Character storageRoom;
    private int hallway;
    private int rack;

    public LocationInfo() {
    }

    public LocationInfo(int productID, char storageRoom, int hallway, int rack) {
        this.productID = productID;
        this.storageRoom = storageRoom;
        this.hallway = hallway;
        this.rack = rack;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    @XmlJavaTypeAdapter(CharacterAdapter.class)
    public Character getStorageRoom() {
        return storageRoom;
    }

    public void setStorageRoom(char storageRoom) {
        this.storageRoom = storageRoom;
    }

    public int getHallway() {
        return hallway;
    }

    public void setHallway(int hallway) {
        this.hallway = hallway;
    }

    public int getRack() {
        return rack;
    }

    public void setRack(int rack) {
        this.rack = rack;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "productID=" + productID +
                ", storageRoom=" + storageRoom +
                ", hallway=" + hallway +
                ", rack=" + rack +
                '}';
    }

    @Override
    public int compareTo(LocationInfo o) {
        return this.getProductID() - o.getProductID();
    }
}
