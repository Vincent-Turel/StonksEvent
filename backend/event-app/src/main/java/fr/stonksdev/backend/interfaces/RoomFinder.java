package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface RoomFinder {
    Room findByName(String name) throws RoomIdNotFoundException;

    Room findById(UUID id) throws RoomIdNotFoundException;

    Optional<Room> findByNameFallible(String name);

    Optional<Room> findByIdFallible(UUID id);
}
