package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;

import java.util.List;

public interface EventModifier {
    StonksEvent create(String name, int maxPeopleAmount, List<Activity> activities);

    boolean modify(Activity activityToModify) throws ItemNotFoundException;

    boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException;
}
