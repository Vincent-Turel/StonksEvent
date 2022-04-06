package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.components.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import java.time.LocalDateTime;
import java.util.Set;

public interface EventModifier {
    StonksEvent create(String name, int maxPeopleAmount, LocalDateTime start, Duration duration) throws AlreadyExistingEventException;

    boolean modify(StonksEvent event, Activity activityToModify) throws EventIdNotFoundException, ActivityNotFoundException;

    boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException;

    Set<StonksEvent> getAllEvents();

    Set<Activity> getActivitiesWithEvent(StonksEvent event) throws EventIdNotFoundException;
}
