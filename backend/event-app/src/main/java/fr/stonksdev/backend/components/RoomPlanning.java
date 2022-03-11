package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.interfaces.RoomExplorer;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RoomPlanning implements RoomExplorer {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Override
    public UUID searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        return finder(roomKind,beginning,duration,minCapacity);
    }

    @Override
    public UUID searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        return finder(null,beginning,duration,minCapacity);
    }

    private boolean checkRoomKind(Map.Entry<UUID, Room> item,RoomKind roomKind){
        if(roomKind==null){
            return true;
        }
        return item.getValue().getRoomKind().equals(roomKind);
    }

    private UUID finder(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException{
        if(!inMemoryDatabase.getRooms().isEmpty()){
            for (Map.Entry<UUID, Room> item : inMemoryDatabase.getRooms().entrySet()) {
                if (checkRoomKind(item,roomKind) && item.getValue().getCapacity()==minCapacity) {
                    List<UUID> listActivity = inMemoryDatabase.getRoomPlanning().get(item.getKey());
                    if(listActivity.isEmpty()){
                        return item.getKey();
                    }
                    boolean isFree = true;
                    for(UUID value: listActivity){
                        if(inMemoryDatabase.getActivities().get(value).getEndDate().isAfter(beginning.plusMinutes(duration.asMinutes()))){
                            isFree = false;
                        }
                    }
                    if(isFree){
                        return item.getKey();
                    }
                }
            }
        }
        throw new RoomNotFoundException();
    }

    @Override
    public UUID searchRoom(String name) throws RoomNotFoundException{
        if(!inMemoryDatabase.getRooms().isEmpty()){
            for (Map.Entry<UUID, Room> item : inMemoryDatabase.getRooms().entrySet()) {
                if(item.getValue().getName().equals(name)){
                    return item.getKey();
                }
            }
        }
        throw new RoomNotFoundException();
    }
}
