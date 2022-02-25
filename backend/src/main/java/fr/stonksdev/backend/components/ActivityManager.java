package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Equipment;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.ActivityModifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ActivityManager implements ActivityModifier {
    @Override
    public Activity create(LocalDateTime beginning, Duration duration, String name, String description, Room room, List<Equipment> requiredEquipment, int maxPeopleAmount){
        return new Activity(beginning, duration, name, description, room, requiredEquipment, maxPeopleAmount);
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
