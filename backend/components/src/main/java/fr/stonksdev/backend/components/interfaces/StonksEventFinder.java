package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.entities.StonksEvent;

import java.util.Optional;

public interface StonksEventFinder {
    Optional<StonksEvent> findByName(String name);

    Optional<StonksEvent> findById(Long id);
}
