package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.interfaces.RoomBooking;
import fr.stonksdev.backend.interfaces.RoomModifier;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

public class RoomManager implements RoomBooking, RoomModifier {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Override
    public boolean bookRoom(UUID roomId, UUID activityId) throws RoomIdNotFoundException, RoomAlreadyBookedException {
        boolean bookComplete = true;
        if(!inMemoryDatabase.getRoomPlanning().isEmpty() && inMemoryDatabase.getRoomPlanning().containsKey(roomId)){
            List<UUID> activityIdList = inMemoryDatabase.getRoomPlanning().get(roomId);
            if(activityIdList.contains(activityId)){
                throw new RoomAlreadyBookedException();
            }
            if(!inMemoryDatabase.getActivities().containsKey(activityId)){
                throw new RoomIdNotFoundException();
            }
            for (UUID value : activityIdList) {
                if(inMemoryDatabase.getActivities().get(value).getEndDate().isBefore(inMemoryDatabase.getActivities().get(activityId).getEndDate())){
                    bookComplete = false;
                }
            }
        }
        if(bookComplete){
            inMemoryDatabase.getRoomPlanning().get(roomId).add(activityId);
        }
        return bookComplete;
    }

    @Override
    public boolean freeRoom(UUID roomId, UUID activityId) throws RoomIdNotFoundException {
        if(!inMemoryDatabase.getRoomPlanning().containsKey(roomId)){
            throw new RoomIdNotFoundException();
        }
        return inMemoryDatabase.getRoomPlanning().get(roomId).remove(activityId);
    }

    @Override
    public boolean create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException {
        if (!inMemoryDatabase.getRooms().isEmpty()) {
            for (Map.Entry<UUID, Room> item : inMemoryDatabase.getRooms().entrySet()) {
                if (item.getValue().getName().equals(name)) {
                    throw new AlreadyExistingRoomException();
                }
            }
        }
        Room newRoom = new Room(name,roomKind,capacity);
        inMemoryDatabase.getRooms().put(newRoom.getId(),newRoom);
        return true;
    }

    @Override
    public boolean modify(UUID roomId, Room newRoom) throws RoomIdNotFoundException{
        if(!inMemoryDatabase.getRooms().containsKey(roomId)){
            throw new RoomIdNotFoundException();
        }
        inMemoryDatabase.getRooms().get(roomId).setName(newRoom.getName());
        inMemoryDatabase.getRooms().get(roomId).setRoomKind(newRoom.getRoomKind());
        inMemoryDatabase.getRooms().get(roomId).setCapacity(newRoom.getCapacity());
        return true;
    }

    @Override
    public boolean delete(UUID roomId) throws RoomIdNotFoundException{
        if(!inMemoryDatabase.getRooms().containsKey(roomId)){
            throw new RoomIdNotFoundException();
        }
        inMemoryDatabase.getRooms().remove(roomId);
        return true;
    }
}
