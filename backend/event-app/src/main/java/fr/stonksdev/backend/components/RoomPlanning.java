package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.interfaces.RoomExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        // So.
        // We tried our best to provide a good implementation of finder. The
        // previous implementation is very weird and basically impossible to
        // understand. See the link below for more.
        // As an interim solution, we put a dummy algorithm which selects the
        // first room it sees.
        //
        // We will provide a correct implementation as part of the next
        // iteration.
        //
        // https://github.com/pns-isa-devops/isa-devops-21-22-team-h-2022/blob/e147b2680f5979af0c581ad31dd0812ed525a4ca/backend/event-app/src/main/java/fr/stonksdev/backend/components/RoomPlanning.java#L43
        return inMemoryDatabase.getRooms().keySet().stream().findFirst().orElseThrow(RoomNotFoundException::new);
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
