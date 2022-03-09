package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.interfaces.RoomBooking;
import fr.stonksdev.backend.interfaces.RoomModifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;

public class RoomManager implements RoomBooking, RoomModifier {

    @Autowired
    private InMemoryDatabase memory;

    @Override
    public boolean bookRoom(String roomId, LocalDateTime beginning, Duration duration, String activityId) {
        return false;
    }

    @Override
    public boolean freeRoom(String roomId, String activityId) {
        return false;
    }

    @Override
    public Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException {
        if (!memory.getRooms().keySet().stream().anyMatch(room -> room.getName().equals(name))){
            throw new AlreadyExistingRoomException();
        }
        Room room = new Room(name, roomKind, capacity);
        memory.getRooms().put(room,new HashSet<>());
        return room;
    }

    @Override
    public boolean modify(String roomId, Room newRoom) throws RoomIdNotFoundException{
        if (memory.getRooms().get(roomId).isEmpty()){
            throw new RoomIdNotFoundException();
        }

        return true;
    }

    @Override
    public boolean delete(String roomId) {
        return false;
    }
}
