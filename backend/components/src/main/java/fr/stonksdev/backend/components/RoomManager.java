package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.components.interfaces.RoomBooking;
import fr.stonksdev.backend.components.interfaces.RoomFinder;
import fr.stonksdev.backend.components.interfaces.RoomModifier;
import fr.stonksdev.backend.components.repositories.PlanningRepository;
import fr.stonksdev.backend.components.repositories.RoomRepository;
import fr.stonksdev.backend.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoomManager implements RoomBooking, RoomModifier, RoomFinder {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private PlanningRepository planningRepo;

    @Autowired RoomPlanning roomPlanning;

    @Override
    public boolean bookRoom(Room room, Activity activity) throws RoomAlreadyBookedException {
        Set<Activity> activities = room.getActivities();

        if (activities.contains(activity))
            throw new RoomAlreadyBookedException();

        for (Activity act : activities) {
            if (act.getEndDate().isAfter(activity.getBeginning()) || act.getBeginning().isBefore(activity.getEndDate()))
                return false;
        }
        room.addActivity(activity);
        return true;
    }

    @Override
    public boolean freeRoom(Room room, Activity activity) {
        activity.setRoom(null);
        return room.getActivities().remove(activity);
    }

    @Override
    @Transactional
    public Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException {
        Room newRoom = new Room(name, roomKind, capacity);
        if (!roomRepo.existsByName(name)) {
            roomRepo.save(newRoom);
            return newRoom;
        } else {
            throw new AlreadyExistingRoomException(name);
        }
    }

    @Override
    @Transactional
    public Room modify(Long roomId, Room newRoom) throws RoomNotFoundException {
        Optional<Room> roomOpt = findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new RoomNotFoundException(roomId.toString());
        }
        Room room = roomOpt.get();
        room.setName(newRoom.getName());
        room.setRoomKind(newRoom.getRoomKind());
        room.setCapacity(newRoom.getCapacity());
        return roomRepo.save(room);
    }

    @Override
    @Transactional
    public void delete(Room room) {
        roomRepo.delete(room);
    }

    @Transactional
    public void updatePlanning(StonksEvent event) throws RoomNotFoundException {
        Optional<Planning> planningOpt = planningRepo.findPlanningByEvent(event);
        Planning planning;
        if (planningOpt.isEmpty()) {
            planning = new Planning(event);
            fillPlanningTable(event);
            event.getActivities().forEach(a -> planning.addSlot(a.generateTimeSlot()));
            planningRepo.save(planning);
        }
    }

    @Override
    public Planning getPlanningOf(StonksEvent event, Room room) throws RoomNotFoundException {
        updatePlanning(event);

        Planning planning = planningRepo.getByEvent(event);

        return planning.getPlanningForRoom(room);
    }

    @Transactional
    public Planning getPlanningOf(StonksEvent event) throws RoomNotFoundException {
        updatePlanning(event);
        var planning = planningRepo.getByEvent(event);
        return planning;
    }

    private void fillPlanningTable(StonksEvent event) throws RoomNotFoundException {
        Set<Activity> activities = event.getActivities();
        for (Activity activity : activities) {
            if (activity.getRoom() == null) {
                bookRoomFor(activity);
            }
        }
    }

    @Transactional
    void bookRoomFor(Activity activity) throws RoomNotFoundException {
        Room room = roomPlanning.searchFreeRoom(activity);

        room.addActivity(activity);
        activity.setRoom(room);
        roomRepo.save(room);
    }

    @Override
    public Optional<Room> findByName(String name) {
        return roomRepo.findRoomByName(name);
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomRepo.findById(id);
    }
}
