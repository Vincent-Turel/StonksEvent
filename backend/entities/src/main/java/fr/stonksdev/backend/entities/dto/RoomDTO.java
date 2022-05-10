package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;



public class RoomDTO {

    public Long id;

    public String name;

    public RoomKind roomKind;

    public int capacity;

    public RoomDTO(Room room) {
        this.name = room.getName();
        this.roomKind = room.getRoomKind();
        this.capacity = room.getCapacity();
    }
}
