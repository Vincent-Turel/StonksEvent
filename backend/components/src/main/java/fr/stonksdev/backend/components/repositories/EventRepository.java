package fr.stonksdev.backend.components.repositories;

import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<StonksEvent, Long> {
    Optional<StonksEvent> findStonksEventByName(String name);
}
