package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;

import java.time.Duration;
import java.time.LocalDateTime;

public interface RoomExplorer {
    Room searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity);

    Room searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity);

    Room searchRoom(String name);

}
