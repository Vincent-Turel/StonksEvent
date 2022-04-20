package fr.stonksdev.backend.components.repositories;

import fr.stonksdev.backend.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findRoomByName(String name);
}
