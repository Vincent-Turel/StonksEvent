package fr.stonksdev.backend.components.repositories;

import fr.stonksdev.backend.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findRoomByName(String name);

    boolean existsByName(String name);
}
