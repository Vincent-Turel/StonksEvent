package fr.stonksdev.backend.components.repositories;

import fr.stonksdev.backend.entities.Planning;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {
    Optional<Planning> findPlanningByEvent(StonksEvent name);

    Planning getByEvent(StonksEvent event);
}
