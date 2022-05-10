package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.entities.Room;

import java.util.Optional;

public interface RoomFinder {
    Optional<Room> findByName(String name);

    Optional<Room> findById(Long id);
}
