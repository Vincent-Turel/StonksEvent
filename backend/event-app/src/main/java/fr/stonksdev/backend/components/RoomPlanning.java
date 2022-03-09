package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.interfaces.RoomExplorer;

import java.time.LocalDateTime;

public class RoomPlanning implements RoomExplorer {
    @Override
    public Room searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) {
        return null;
    }

    @Override
    public Room searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity) {
        return null;
    }

    @Override
    public Room searchRoom(String name) {
        return null;
    }
}
