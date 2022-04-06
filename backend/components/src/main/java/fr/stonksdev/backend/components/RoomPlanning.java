package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.components.interfaces.RoomExplorer;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class RoomPlanning implements RoomExplorer {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Override
    public UUID searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        return finder(Optional.of(roomKind), beginning, duration, minCapacity);
    }

    @Override
    public UUID searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        return finder(Optional.empty(),beginning, duration, minCapacity);
    }

    private boolean checkRoomKind(Map.Entry<UUID, Room> item, Optional<RoomKind> roomKind) {
        if (roomKind.isEmpty()) {
            return true;
        }
        return item.getValue().getRoomKind().equals(roomKind.get());
    }

    private UUID finder(Optional<RoomKind> roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        UUID find = null;
        int size = -1;
        if (!inMemoryDatabase.getRooms().isEmpty()) {
            for (Map.Entry<UUID, Room> item : inMemoryDatabase.getRooms().entrySet()) {
                if (checkRoomKind(item, roomKind) && item.getValue().getCapacity() >= minCapacity && (size == -1 || size > item.getValue().getCapacity())) {
                    List<UUID> listActivity = inMemoryDatabase.getRoomPlanning().get(item.getKey());
                    if (listActivity == null || listActivity.isEmpty()) {
                        size = item.getValue().getCapacity();
                        find = item.getKey();
                    } else {
                        boolean isFree = true;
                        for (UUID value : listActivity) {
                            if (inMemoryDatabase.getActivities().get(value).getEndDate().isAfter(beginning)) {
                                isFree = false;
                            }
                        }
                        if (isFree) {
                            size = item.getValue().getCapacity();
                            find = item.getKey();
                        }
                    }
                }
            }
        }
        if (size != -1 && find != null) {
            return find;
        }
        throw new RoomNotFoundException();
    }

    @Override
    public UUID searchRoom(String name) throws RoomNotFoundException {
        if (!inMemoryDatabase.getRooms().isEmpty()) {
            for (Map.Entry<UUID, Room> item : inMemoryDatabase.getRooms().entrySet()) {
                if (item.getValue().getName().equals(name)) {
                    return item.getKey();
                }
            }
        }
        throw new RoomNotFoundException();
    }
}
