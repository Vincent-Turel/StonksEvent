package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.ActivityModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityManager implements ActivityModifier {
    @Autowired
    private InMemoryDatabase memory;

    @Override
    public Activity create(StonksEvent stonksEvent, LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount) {
        Activity activity = new Activity(beginning, duration, name, maxPeopleAmount);
        memory.getActivities().get(stonksEvent).add(activity);
        return activity;
    }

    @Override
    public boolean modify(Activity activityToModify, Activity activityModified) throws ItemNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Activity activityToDelete) throws ItemNotFoundException {
        return false;
    }

}
