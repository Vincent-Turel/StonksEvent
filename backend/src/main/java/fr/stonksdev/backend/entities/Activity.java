package fr.stonksdev.backend.entities;

import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.ActivityModifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Activity implements ActivityModifier {
    @Override
    public StonksEvent create(LocalDateTime beginning, Duration duration, String name, String description, Room room, List<Equipment> requiredEquipment, int maxPeopleAmount) {
        return null;
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
