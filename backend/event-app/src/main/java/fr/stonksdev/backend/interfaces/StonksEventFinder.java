package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.StonksEvent;

import java.util.Optional;
import java.util.UUID;

public interface StonksEventFinder {

    Optional<StonksEvent> findByName(String name);

    Optional<StonksEvent> findById(UUID id);
}
