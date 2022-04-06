package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.entities.Room;

import java.util.Optional;
import java.util.UUID;

public interface RoomFinder {
    Room findByName(String name) throws RoomIdNotFoundException;

    Room findById(UUID id) throws RoomIdNotFoundException;

    Optional<Room> findByNameFallible(String name);

    Optional<Room> findByIdFallible(UUID id);
}
