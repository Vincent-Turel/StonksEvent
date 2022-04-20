package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.components.interfaces.RoomBooking;
import fr.stonksdev.backend.components.interfaces.RoomFinder;
import fr.stonksdev.backend.components.interfaces.RoomModifier;
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
        return room.getActivities().remove(activity);
    }

    @Override
    @Transactional
    public Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException {
        Room newRoom = new Room(name, roomKind, capacity);
        roomRepo.save(newRoom);
        return newRoom;
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

    @Override
    public List<TimeSlot> getPlanningOf(StonksEvent event, Room room) throws RoomNotFoundException {
        fillPlanningTable(event);

        List<Activity> roomActivities = new ArrayList<>(room.getActivities());

        List<Activity> activities = new ArrayList<>(roomActivities.size());

        for (Activity activity : roomActivities) {
            if (Objects.isNull(activity) || activity.getEvent().equals(event)) {
                continue;
            }

            activities.add(activity);
        }

        activities.sort(Comparator.comparing(Activity::getBeginning));

        return activities.stream().map(activity -> new TimeSlot(activity.getName(), activity.getBeginning(), activity.getDuration())).collect(Collectors.toList());
    }

    public Map<String, List<TimeSlot>> getPlanningOf(StonksEvent event) throws RoomNotFoundException {
        fillPlanningTable(event);

        Map<String, List<TimeSlot>> assoc = new HashMap<>();

        for (Room room : roomRepo.findAll()) {
            List<TimeSlot> activities = new ArrayList<>();

            for (Activity activity : room.getActivities()) {
                TimeSlot timeSlot = activity.generateTimeSlot();
                activities.add(timeSlot);
            }

            assoc.put(room.getName(), activities);
        }

        return assoc;
    }

    private void fillPlanningTable(StonksEvent event) throws RoomNotFoundException {
        Set<Activity> activities = event.getActivities();
        for (Activity activity : activities) {
            if (!aRoomIsBookedFor(activity)) {
                bookRoomFor(activity);
            }
        }
    }

    private boolean aRoomIsBookedFor(Activity activity) {
        return roomRepo.findAll()
                .stream()
                .anyMatch(room -> room.getActivities().contains(activity));
    }

    private void bookRoomFor(Activity activity) throws RoomNotFoundException {
        // TODO add complexity (don't just get first room)
        Room room = roomRepo.findAll().stream().findFirst().orElseThrow(RoomNotFoundException::new);

        room.addActivity(activity);
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
