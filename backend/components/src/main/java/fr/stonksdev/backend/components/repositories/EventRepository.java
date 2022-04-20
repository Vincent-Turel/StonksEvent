package fr.stonksdev.backend.components.repositories;

import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<StonksEvent, Long> {
    Optional<StonksEvent> findStonksEventByName(String name);
}
