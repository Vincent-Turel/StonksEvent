package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.interfaces.RoomBooking;
import fr.stonksdev.backend.interfaces.RoomModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoomManager implements RoomBooking, RoomModifier {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    private final List<UUID> listRoomId = new ArrayList<>();

    @Override
    public boolean bookRoom(UUID roomId, UUID activityId) throws ActivityNotFoundException, RoomAlreadyBookedException {
        boolean bookComplete = true;

        if(!inMemoryDatabase.getRoomPlanning().containsKey(roomId)){
            List<UUID> newList = new ArrayList<>();
            inMemoryDatabase.getRoomPlanning().put(roomId,newList);
        }

        if(!inMemoryDatabase.getRoomPlanning().isEmpty()){
            List<UUID> activityIdList = inMemoryDatabase.getRoomPlanning().get(roomId);
            if(activityIdList.contains(activityId)){
                throw new RoomAlreadyBookedException();
            }
            if(!inMemoryDatabase.getActivities().containsKey(activityId)){
                throw new ActivityNotFoundException();
            }
            for (UUID value : activityIdList) {
                if(!inMemoryDatabase.getActivities().get(value).getEndDate().isBefore(inMemoryDatabase.getActivities().get(activityId).getBeginning())){
                    bookComplete = false;
                }
            }
        }

        if(bookComplete){
            inMemoryDatabase.getRoomPlanning().get(roomId).add(activityId);
            return bookComplete;
        }
        else {
            throw new RoomAlreadyBookedException();
        }
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
        listRoomId.add(newRoom.getId());
        return true;
    }

    @Override
    public boolean modify(UUID roomId, Room newRoom) throws RoomIdNotFoundException{
        if(!inMemoryDatabase.getRooms().containsKey(roomId)){
            throw new RoomIdNotFoundException();
        }
        Room room = inMemoryDatabase.getRooms().get(roomId);
        room.setName(newRoom.getName());
        room.setRoomKind(newRoom.getRoomKind());
        room.setCapacity(newRoom.getCapacity());
        return true;
    }

    @Override
    public boolean delete(UUID roomId) throws RoomIdNotFoundException{
        if(!inMemoryDatabase.getRooms().containsKey(roomId)){
            throw new RoomIdNotFoundException();
        }
        listRoomId.remove(roomId);
        inMemoryDatabase.getRooms().remove(roomId);
        return true;
    }

    public List<UUID> getListRoomId(){
        return listRoomId;
    }

    public void reset(){
        listRoomId.clear();
        inMemoryDatabase.getRoomPlanning().clear();
        inMemoryDatabase.getRooms().clear();
    }
}
