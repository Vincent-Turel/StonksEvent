package fr.stonksdev.backend.entities;

import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.EventModifier;

import java.util.List;

public class StonksEvent implements EventModifier {
    @Override
    public StonksEvent create(String name, Room room, int maxPeopleAmount, List<Activity> activities, List<Equipment> requiredEquipment) {
        return null;
    }

    @Override
    public boolean modify(StonksEvent eventToModify, StonksEvent eventModified) throws ItemNotFoundException {
        return false;
    }

    @Override
    public boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException {
        return false;
    }
}
