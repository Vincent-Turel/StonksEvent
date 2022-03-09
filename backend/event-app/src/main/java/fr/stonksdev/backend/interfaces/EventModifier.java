package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

public interface EventModifier {
    StonksEvent create(String name, int maxPeopleAmount, LocalDateTime start, Duration duration) throws AlreadyExistingEventException;

    boolean modify(String eventId, Activity activityToModify) throws EventIdNotFoundException, ActivityNotFoundException;

    boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException;

    Set<StonksEvent> getAllEvents();

    Set<Activity> getActivitiesWithEvent(String eventId) throws EventIdNotFoundException;
}
