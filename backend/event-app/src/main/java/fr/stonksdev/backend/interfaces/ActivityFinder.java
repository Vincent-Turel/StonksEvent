package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface ActivityFinder {
    Activity findByName(String name) throws ActivityNotFoundException;

    Activity findById(UUID id) throws ActivityNotFoundException;

    Optional<Activity> findByNameFallible(String name);

    Optional<Activity> findByIdFallible(UUID id);
}
