package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.entities.StonksEvent;

import java.util.Optional;
import java.util.UUID;

public interface StonksEventFinder {
    StonksEvent findByName(String name) throws EventIdNotFoundException;

    StonksEvent findById(UUID id) throws EventIdNotFoundException;

    Optional<StonksEvent> findByNameFallible(String name);

    Optional<StonksEvent> findByIdFallible(UUID id);
}
