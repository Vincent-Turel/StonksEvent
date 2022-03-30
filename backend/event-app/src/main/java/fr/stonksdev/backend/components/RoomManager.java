package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.*;
import fr.stonksdev.backend.exceptions.*;
import fr.stonksdev.backend.interfaces.RoomBooking;
import fr.stonksdev.backend.interfaces.RoomFinder;
import fr.stonksdev.backend.interfaces.RoomModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoomManager implements RoomBooking, RoomModifier, RoomFinder {
    private final List<UUID> listRoomId = new ArrayList<>();

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Autowired
    private RoomPlanning roomPlanning;

    @Override
    public boolean bookRoom(Room room, Activity activity) throws ActivityNotFoundException, RoomAlreadyBookedException {
        boolean bookComplete = true;

        UUID roomId = room.getId();
        UUID activityId = activity.getActivityID();

        if (!inMemoryDatabase.getRoomPlanning().containsKey(roomId)) {
            List<UUID> newList = new ArrayList<>();
            inMemoryDatabase.getRoomPlanning().put(roomId, newList);
        }

        if (!inMemoryDatabase.getRoomPlanning().isEmpty()) {
            List<UUID> activityIdList = inMemoryDatabase.getRoomPlanning().get(roomId);
            if (activityIdList.contains(activityId)) {
                throw new RoomAlreadyBookedException();
            }
            if (!inMemoryDatabase.getActivities().containsKey(activityId)) {
                throw new ActivityNotFoundException();
            }
            for (UUID value : activityIdList) {
                if (!inMemoryDatabase.getActivities().get(value).getEndDate().isBefore(inMemoryDatabase.getActivities().get(activityId).getBeginning())) {
                    bookComplete = false;
                }
            }
        }

        if (bookComplete) {
            inMemoryDatabase.getRoomPlanning().get(roomId).add(activityId);
            return bookComplete;
        } else {
            throw new RoomAlreadyBookedException();
        }
    }

    @Override
    public boolean freeRoom(Room room, Activity activity) throws RoomIdNotFoundException {
        UUID roomId = room.getId();
        UUID activityId = activity.getActivityID();

        if (!inMemoryDatabase.getRoomPlanning().containsKey(roomId)) {
            throw new RoomIdNotFoundException();
        }
        return inMemoryDatabase.getRoomPlanning().get(roomId).remove(activityId);
    }

    @Override
    public boolean create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException {
        if (!inMemoryDatabase.getRooms().isEmpty()) {
            for (Map.Entry<UUID, Room> item : inMemoryDatabase.getRooms().entrySet()) {
                if (item.getValue().getName().equals(name)) {
                    throw new AlreadyExistingRoomException();
                }
            }
        }
        Room newRoom = new Room(name, roomKind, capacity);
        inMemoryDatabase.getRooms().put(newRoom.getId(), newRoom);
        listRoomId.add(newRoom.getId());
        return true;
    }

    @Override
    public boolean modify(Room room, Room newRoom) throws RoomIdNotFoundException {
        UUID roomId = room.getId();

        if (!inMemoryDatabase.getRooms().containsKey(roomId)) {
            throw new RoomIdNotFoundException();
        }
        room.setName(newRoom.getName());
        room.setRoomKind(newRoom.getRoomKind());
        room.setCapacity(newRoom.getCapacity());
        return true;
    }

    @Override
    public boolean delete(Room room) throws RoomIdNotFoundException {
        UUID roomId = room.getId();

        if (!inMemoryDatabase.getRooms().containsKey(roomId)) {
            throw new RoomIdNotFoundException();
        }
        listRoomId.remove(roomId);
        inMemoryDatabase.getRooms().remove(roomId);
        return true;
    }

    public List<UUID> getListRoomId() {
        return listRoomId;
    }

    @Override
    public List<TimeSlot> getPlanningOf(StonksEvent event, Room room) throws RoomNotFoundException {
        UUID eventId = event.getId();
        fillPlanningTable(eventId);
        UUID roomId = room.getId();

        List<UUID> activityIDs = inMemoryDatabase.getRoomPlanning().get(roomId);

        if (Objects.isNull(activityIDs)) {
            return List.of();
        }

        ArrayList<Activity> activities = new ArrayList<>(activityIDs.size());

        for (UUID activityId : activityIDs) {
            Activity activity = inMemoryDatabase.getActivities().get(activityId);

            if (Objects.isNull(activity) || activity.getEventId().equals(eventId)) {
                continue;
            }

            activities.add(activity);
        }

        activities.sort(Comparator.comparing(Activity::getBeginning));

        return activities.stream().map(activity -> new TimeSlot(activity.getName(), activity.getBeginning(), activity.getDuration())).collect(Collectors.toList());
    }

    public Map<String, List<TimeSlot>> getPlanningOf(StonksEvent event) throws ActivityNotFoundException, RoomIdNotFoundException, RoomNotFoundException {
        UUID eventId = event.getId();

        fillPlanningTable(eventId);

        HashMap<String, List<TimeSlot>> assoc = new HashMap<>();

        for (Map.Entry<UUID, List<UUID>> entry : inMemoryDatabase.getRoomPlanning().entrySet()) {
            UUID roomId = entry.getKey();
            ArrayList<TimeSlot> activities = new ArrayList<>();

            for (UUID activityId : entry.getValue()) {
                Activity activity = inMemoryDatabase.getActivities().get(activityId);

                if (Objects.isNull(activity)) {
                    throw new ActivityNotFoundException();
                }

                TimeSlot timeSlot = new TimeSlot(activity.getName(), activity.getBeginning(), activity.getDuration());
                activities.add(timeSlot);
            }

            Room room = inMemoryDatabase.getRooms().get(roomId);

            if (Objects.isNull(room)) {
                throw new RoomIdNotFoundException();
            }

            assoc.put(room.getName(), activities);
        }

        return assoc;
    }

    public void reset() {
        listRoomId.clear();
        inMemoryDatabase.getRoomPlanning().clear();
        inMemoryDatabase.getRooms().clear();
    }

    private void fillPlanningTable(UUID eventId) throws RoomNotFoundException {
        Set<Activity> activities = inMemoryDatabase.getEventActivityAssociation().get(eventId);

        if (Objects.isNull(activities)) {
            // Having an empty slot here means that we have no associated
            // activity. That's ok.
            activities = new HashSet<>();
        }
        for (Activity activity : activities) {
            if (!aRoomIsBookedFor(activity)) {
                bookRoomFor(activity);
            }
        }

    }

    private boolean aRoomIsBookedFor(Activity activity) {
        return inMemoryDatabase
                .getRoomPlanning()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(id -> id.equals(activity.getActivityID()));
    }

    private void bookRoomFor(Activity activity) throws RoomNotFoundException {
        UUID roomId = inMemoryDatabase.getRooms().keySet().stream().findFirst().orElseThrow(RoomNotFoundException::new);

        if (!inMemoryDatabase.getRoomPlanning().containsKey(roomId)) {
            inMemoryDatabase.getRoomPlanning().put(roomId, new ArrayList<>());
        }

        // This can not be null because we have checked for such case in the
        // previous statement.
        List<UUID> roomActivities = inMemoryDatabase.getRoomPlanning().get(roomId);

        roomActivities.add(activity.getActivityID());
    }

    private UUID getEventId(String eventName) throws EventIdNotFoundException {
        return inMemoryDatabase
                .getEvents()
                .values()
                .stream()
                .filter(event -> event.getName().equals(eventName))
                .findFirst()
                .map(StonksEvent::getId)
                .orElseThrow(EventIdNotFoundException::new);
    }

    private UUID getRoomId(String roomName) throws RoomIdNotFoundException {
        return inMemoryDatabase
                .getRooms()
                .values()
                .stream()
                .filter(room -> room.getName().equals(roomName))
                .findFirst()
                .map(Room::getId)
                .orElseThrow(RoomIdNotFoundException::new);
    }

    @Override
    public Optional<Room> findByNameFallible(String name) {
        return inMemoryDatabase
                .getRooms()
                .values()
                .stream()
                .filter(room -> room.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Room> findByIdFallible(UUID id) {
        return Optional.ofNullable(inMemoryDatabase.getRooms().get(id));
    }

    @Override
    public Room findByName(String name) throws RoomIdNotFoundException {
        return findByNameFallible(name).orElseThrow(RoomIdNotFoundException::new);
    }

    @Override
    public Room findById(UUID id) throws RoomIdNotFoundException {
        return findByIdFallible(id).orElseThrow(RoomIdNotFoundException::new);
    }
}
