package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Equipment;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;

import java.util.List;

public interface EventModifier {
    StonksEvent create(String name, Room room, int maxPeopleAmount, List<Activity> activities, List<Equipment> requiredEquipment);

    boolean modify(StonksEvent eventToModify, StonksEvent eventModified) throws ItemNotFoundException;

    boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException;
}
