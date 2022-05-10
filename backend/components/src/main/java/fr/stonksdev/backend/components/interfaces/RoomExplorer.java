package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;

import java.time.LocalDateTime;

public interface RoomExplorer {

    Room searchFreeRoom(RoomKind roomKind, Activity activity) throws RoomNotFoundException;

    Room searchFreeRoom(Activity activity) throws RoomNotFoundException;
}
