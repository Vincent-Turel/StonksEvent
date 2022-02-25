package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Equipment;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityModifier {
    Activity create(LocalDateTime beginning, Duration duration, String name, String description, Room room,
                       List<Equipment> requiredEquipment, int maxPeopleAmount);

    boolean modify(Activity activityToModify, Activity activityModified) throws ItemNotFoundException;

    boolean delete(Activity activityToDelete) throws ItemNotFoundException;
}
