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
import java.util.UUID;

public class RoomManager implements RoomBooking, RoomModifier {

    @Autowired
    private InMemoryDatabase memory;

    @Override
    public boolean bookRoom(UUID roomId, LocalDateTime beginning, Duration duration, String activityId) {
        return false;
    }

    @Override
    public boolean freeRoom(UUID roomId, String activityId) {
        return false;
    }

    @Override
    public Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException {
        if (memory.getRooms().values().stream().noneMatch(room -> room.getName().equals(name))){
            throw new AlreadyExistingRoomException();
        }
        Room room = new Room(name, roomKind, capacity);
        memory.getRooms().put(room.getId(),room);
        return room;
    }

    @Override
    public boolean modify(UUID roomId, Room newRoom) throws RoomIdNotFoundException{
        Room room = memory.getRooms().get(roomId);
        if (room != null){
            room.setCapacity(newRoom.getCapacity());
            room.setRoomKind(newRoom.getRoomKind());
            room.setName(newRoom.getName());
            return true;
        }
        throw new RoomIdNotFoundException();
    }

    @Override
    public boolean delete(UUID roomId) throws RoomIdNotFoundException{
        Room room = memory.getRooms().remove(roomId);
        if(room != null){
            return true;
        }
        throw new RoomIdNotFoundException();
    }
}
