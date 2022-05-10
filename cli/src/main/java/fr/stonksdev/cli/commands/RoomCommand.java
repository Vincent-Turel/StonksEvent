package fr.stonksdev.cli.commands;

import fr.stonksdev.cli.exceptions.CastingTimeSlotsException;
import fr.stonksdev.cli.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeParseException;
import java.util.Objects;

@ShellComponent
public class RoomCommand {

    private static final String PLANNING_BASE_URI = "/planning";
    private static final String ROOM_BASE_URI = "/room";

    @Autowired
    RestTemplate restTemplate;

    @ShellMethod("Create a new room (create-room ROOM_NAME KIND_OF_ROOM CAPACITY)")
    public Room createRoom(String name, RoomKind kind, int capacity) {
        return restTemplate.postForObject(ROOM_BASE_URI, new Room(name, kind, capacity), Room.class);
    }

    @ShellMethod("Associate an event with his room planning and return the planning for a specific room (planning-event-room EVENT_NAME ROOM_NAME)\n")
    public String associatePlanningRoomEvent(String eventName, String roomName) throws DateTimeParseException, CastingTimeSlotsException {
        Planning planning = restTemplate.getForObject(PLANNING_BASE_URI + "/" + eventName + "/" + roomName, Planning.class);
        if (Objects.isNull(planning)) {
            throw new CastingTimeSlotsException("Json parsing failed. Check associatePlanningRoomEvent method in RoomCommand");
        }
        System.out.println("associatePlanningRoomEvent : planning is not NULL ");
        System.out.println("Planning to String : "+ planning);
        return planning.toString();
    }

    @ShellMethod("Associate an event with his room planning and return the planning for the event (planning-event EVENT_NAME)\n")
    public String associatePlanningEvent(String eventName) throws DateTimeParseException, CastingTimeSlotsException {
        Planning planning = restTemplate.getForObject(PLANNING_BASE_URI + "/" + eventName, Planning.class);
        if (Objects.isNull(planning)) {
            throw new CastingTimeSlotsException("Json parsing failed. Check associatePlanningEvent method in RoomCommand");
        }
        System.out.println("associatePlanningEvent : planning is not NULL ");
        System.out.println("Planning to String : "+ planning);
        return planning.toString();
    }

}
