package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;

import java.time.LocalDateTime;

public interface ActivityModifier {
    Activity create(StonksEvent stonksEvent, LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount);

    boolean modify(Activity activityToModify, Activity activityModified) throws ItemNotFoundException;

    boolean delete(Activity activityToDelete) throws ItemNotFoundException;
}
