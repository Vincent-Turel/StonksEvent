package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.interfaces.ActivityFinder;
import fr.stonksdev.backend.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ActivityRegistry implements ActivityFinder {
    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Override
    public Activity findByName(String name) throws ActivityNotFoundException {
        return findByNameFallible(name).orElseThrow(ActivityNotFoundException::new);
    }

    @Override
    public Activity findById(UUID id) throws ActivityNotFoundException {
        return findByIdFallible(id).orElseThrow(ActivityNotFoundException::new);
    }

    @Override
    public Optional<Activity> findByNameFallible(String name) {
        return inMemoryDatabase.getActivities().values().stream().filter(activity -> activity.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<Activity> findByIdFallible(UUID id) {
        return Optional.ofNullable(inMemoryDatabase.getActivities().get(id));
    }
}
