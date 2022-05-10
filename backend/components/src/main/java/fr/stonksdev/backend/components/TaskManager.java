package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.components.interfaces.TaskGenerator;
import fr.stonksdev.backend.components.repositories.TaskRepository;
import fr.stonksdev.backend.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TaskManager implements TaskGenerator {
    @Autowired
    private RoomManager roomManager;

    @Autowired
    private TaskRepository taskRepo;

    @Override
    public List<Task> tasksForEvent(StonksEvent event) throws RoomNotFoundException, ActivityNotFoundException {
        // In the future, we should add more tasks here.
        var tasks = getCleaningTasks(event);
        taskRepo.deleteAll();
        return taskRepo.saveAll(tasks);
    }

    List<Task> getCleaningTasks(StonksEvent event) throws RoomNotFoundException, ActivityNotFoundException {
        Planning planning = roomManager.getPlanningOf(event);

        Map<String, List<TimeSlot>> aggregatedPlanning = aggregatePlanning(planning);

        List<Task> beforeEvent = cleaningBeforeEvent(aggregatedPlanning);
        List<Task> betweenActivities = cleaningBetweenActivities(aggregatedPlanning);
        List<Task> afterEvent = cleaningAfterEvent(aggregatedPlanning);

        return Stream.of(beforeEvent, betweenActivities, afterEvent).flatMap(Collection::stream).collect(Collectors.toList());
    }

    Map<String, List<TimeSlot>> aggregatePlanning(Planning planning) {
        Map<String, List<TimeSlot>> slots = new HashMap<>();

        planning
                .getTimeSlots()
                .forEach(slot -> {
                    String roomName = slot.getActivity().getRoom().getName();
                    List<TimeSlot> slotsForRoom = slots.get(roomName);

                    if (Objects.isNull(slotsForRoom)) {
                        slotsForRoom = new ArrayList<>();
                        slots.put(roomName, slotsForRoom);
                    }

                    slotsForRoom.add(slot);
                });

        return slots;
    }

    List<Task> cleaningBeforeEvent(Map<String, List<TimeSlot>> planning) throws ActivityNotFoundException {
        ArrayList<Task> collector = new ArrayList<>();

        for (Map.Entry<String, List<TimeSlot>> entry : planning.entrySet()) {
            String roomName = entry.getKey();
            List<TimeSlot> activities = entry.getValue();

            TimeSlot firstActivitySlot = activities
                    .stream()
                    .min(Comparator.comparing(TimeSlot::getBeginning))
                    .orElseThrow(ActivityNotFoundException::new);

            LocalDateTime start = firstActivitySlot.getBeginning();

            collector.add(Task.cleaning(roomManager.findByName(roomName).get(), TaskTimeBound.before(start)));
        }

        return collector;
    }

    List<Task> cleaningBetweenActivities(Map<String, List<TimeSlot>> planning) {
        ArrayList<Task> collector = new ArrayList<>();
        for (Map.Entry<String, List<TimeSlot>> entry : planning.entrySet()) {
            String name = entry.getKey();

            List<TimeSlot> slots = new ArrayList<>(entry.getValue());
            slots.sort(Comparator.naturalOrder());

            if (slots.isEmpty()) {
                return List.of();
            }

            // I'm sorry.
            TimeSlot prev = null;

            for (TimeSlot next : slots) {
                if (Objects.isNull(prev)) {
                    prev = next;
                    continue;
                }

                // At this point, `prev` and `next` respectively correspond
                // to the activity *before* and *after* the task that is about
                // to be created.

                TaskTimeBound bound = TaskTimeBound.between(prev.end(), next.getBeginning());

                Task cleaningBetweenActivities = Task.cleaning(roomManager.findByName(name).get(), bound);

                collector.add(cleaningBetweenActivities);
            }
        }

        return collector;
    }

    List<Task> cleaningAfterEvent(Map<String, List<TimeSlot>> planning) throws ActivityNotFoundException {
        // Wise reader will notice that this method is kinda similar to
        // TaskManager::cleaningBeforeEvent.

        ArrayList<Task> collector = new ArrayList<>();

        for (Map.Entry<String, List<TimeSlot>> entry : planning.entrySet()) {
            String roomName = entry.getKey();
            List<TimeSlot> activities = entry.getValue();

            TimeSlot lastActivitySlot = activities
                    .stream()
                    .max(Comparator.comparing(TimeSlot::getBeginning))
                    .orElseThrow(ActivityNotFoundException::new);

            LocalDateTime end = lastActivitySlot.end();

            collector.add(Task.cleaning(roomManager.findByName(roomName).get(), TaskTimeBound.after(end)));
        }

        return collector;
    }
}
