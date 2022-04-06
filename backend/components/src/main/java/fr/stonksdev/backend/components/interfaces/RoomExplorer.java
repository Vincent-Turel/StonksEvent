package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;


import java.time.LocalDateTime;
import java.util.UUID;

public interface RoomExplorer {
    UUID searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException, RoomNotFoundException;

    UUID searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException;

    UUID searchRoom(String name) throws RoomNotFoundException;

}
