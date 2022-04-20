package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.entities.Activity;

import java.util.Optional;

public interface ActivityFinder {
    Optional<Activity> findByName(String name);

    Optional<Activity> findById(Long id);
}
