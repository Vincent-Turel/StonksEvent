package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;

import java.time.LocalDateTime;

public interface RoomExplorer {
    Room searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException;

    Room searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException;

}
