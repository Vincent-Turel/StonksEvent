package fr.stonksdev.backend.entities;

public class Room extends Place {
    private String name;
    private RoomKind roomKind;
    private int capacity;
    //private List<Equipment> ==> A REFLECHIR

    public Room(String name, RoomKind roomKind, int capacity) {
        this.name = name;
        this.roomKind = roomKind;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomKind getRoomKind() {
        return roomKind;
    }

    public void setRoomKind(RoomKind roomKind) {
        this.roomKind = roomKind;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
