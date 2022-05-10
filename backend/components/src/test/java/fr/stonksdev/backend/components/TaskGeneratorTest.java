package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.components.interfaces.TaskGenerator;
import fr.stonksdev.backend.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class TaskGeneratorTest {
    @Autowired
    private StonksEventManager eventManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private TaskGenerator taskGenerator;
    @Autowired
    private RoomManager roomManager;
    @MockBean
    private MailProxy mailMock;

    private StonksEvent event;
    private Activity activity1;
    private Activity activity2;

    private final LocalDateTime eventStartDate = LocalDateTime.of(2022, 8, 4, 9, 0, 0);
    private final LocalDateTime eventEndDate = LocalDateTime.of(2022, 8, 5, 19, 0, 0);

    private final LocalDateTime activity1StartDate = LocalDateTime.of(2022, 8, 4, 12, 0, 0);
    private final Duration activity1Duration = Duration.ofMinutes(30);

    private final LocalDateTime activity2StartDate = LocalDateTime.of(2022, 8, 4, 18, 0, 0);
    private final Duration activity2Duration = Duration.ofMinutes(30);

    private Planning planning;
    private List<Task> tasks;

    @BeforeEach
    void setup() throws AlreadyExistingEventException, AlreadyExistingActivityException, RoomNotFoundException, ActivityNotFoundException, AlreadyExistingRoomException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        roomManager.create("Salle O+303", RoomKind.Classroom, 30);
        roomManager.create("Salle O+318", RoomKind.Classroom, 35);

        event = eventManager.createEvent("RustConf 2022", 150, eventStartDate, eventEndDate);
        activity1 = activityManager.createActivity(activity1StartDate, activity1Duration, "Sasha's Talk", 25, event);
        activity2 = activityManager.createActivity(activity2StartDate, activity2Duration, "Mara Bos' Talk", 25, event);

        planning = roomManager.getPlanningOf(event);
        tasks = taskGenerator.tasksForEvent(event);
    }

    @Test
    void tasksAreCreated() {
        assertEquals(3, tasks.size());
    }

    // We can't really hardcode data in these tests, as we're not supposed to
    // know how the planning will look like. As such, we're only guessing
    // properties of the task set based on the planning structure.

    @Test
    void cleanupAtTheBeginningIsPresent() {
        for (var roomUsed : roomsUsedForEvent()) {
            var firstActivityInTheRoom = roomUsed.getActivities().stream().min(Comparator.comparing(Activity::getBeginning)).get();
            var initialCleaningTaskThatShouldBeCreated = Task.cleaning(roomUsed, TaskTimeBound.before(firstActivityInTheRoom.getBeginning()));

            assertTrue(tasks.contains(initialCleaningTaskThatShouldBeCreated));
        }
    }

    @Test
    void cleanupBetweenActivitiesIsPresent() {
        for (var roomUsed : roomsUsedForEvent()) {
            var activitiesInTheRoom = planning.getPlanningForRoom(roomUsed);

            activitiesInTheRoom.getTimeSlots().sort(Comparator.comparing(TimeSlot::getBeginning));

            TimeSlot previousActivity = null;

            for (TimeSlot nextActivity : activitiesInTheRoom.getTimeSlots()) {
                if (Objects.isNull(previousActivity)) {
                    previousActivity = nextActivity;
                    continue;
                }

                var previousTime = previousActivity.end();
                var nextTime = nextActivity.getBeginning();

                var cleaningTaskBetweenActivities = Task.cleaning(roomUsed, TaskTimeBound.between(previousTime, nextTime));

                assertTrue(tasks.contains(cleaningTaskBetweenActivities));
            }
        }
    }

    @Test
    void cleanupAtTheEndIsPresent() {
        for (var roomUsed : roomsUsedForEvent()) {
            var lastActivityInTheRoom = roomUsed.getActivities().stream().max(Comparator.comparing(Activity::getBeginning)).get();
            var initialCleaningTaskThatShouldBeCreated = Task.cleaning(roomUsed, TaskTimeBound.after(lastActivityInTheRoom.getEndDate()));

            assertTrue(tasks.contains(initialCleaningTaskThatShouldBeCreated));
        }
    }

    // This is a little helper that will gather information that are useful
    // for the task set but slightly hidden in the planning data structure.
    Set<Room> roomsUsedForEvent() {
        return planning.getTimeSlots().stream().map(TimeSlot::getActivity).map(Activity::getRoom).collect(Collectors.toSet());
    }
}
