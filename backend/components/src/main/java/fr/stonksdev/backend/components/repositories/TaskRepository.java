package fr.stonksdev.backend.components.repositories;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.Task;
import fr.stonksdev.backend.entities.TaskTimeBound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByRoomAndTimeBound(Room room, TaskTimeBound taskTimeBound);
}
