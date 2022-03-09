package fr.stonksdev.backend.entities;

import java.util.UUID;

public class Room extends Place {
    private UUID id;
    private String name;
    private RoomKind roomKind;
    private int capacity;

    public Room(String name, RoomKind roomKind, int capacity) {
        this.name = name;
        this.roomKind = roomKind;
        this.capacity = capacity;
        id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }
}
