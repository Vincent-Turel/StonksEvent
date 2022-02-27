package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;

import java.util.Date;

public interface EventModifier {
    StonksEvent create(String name, int maxPeopleAmount, Date startDate, Date endDate);

    boolean modify(StonksEvent eventToModify, StonksEvent eventModified) throws ItemNotFoundException;

    boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException;
}
