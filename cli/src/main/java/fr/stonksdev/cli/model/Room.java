package fr.stonksdev.cli.model;

import java.util.Objects;

public class Room {

    public Long id;

    public String name;

    public RoomKind roomKind;

    public int capacity;

    public Room(String name, RoomKind roomKind, int capacity) {
        this.name = name;
        this.roomKind = roomKind;
        this.capacity = capacity;
    }

    public Room() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return capacity == room.capacity && Objects.equals(id, room.id) && Objects.equals(name, room.name) && roomKind == room.roomKind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roomKind, capacity);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomKind=" + roomKind +
                ", capacity=" + capacity +
                '}';
    }
}

