package fr.stonksdev.backend.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
public class RoomPlanningAdvancedTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private StonksEventManager activityManager;
    @Autowired
    private RoomPlanning planning;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException, RoomAlreadyBookedException, ActivityNotFoundException {
        activityManager.reset();
        roomManager.reset();
        roomManager.create("Salle 101", RoomKind.Classroom, 30);
        roomManager.create("Salle 102", RoomKind.Classroom, 50);
        roomManager.create("AmphiOuest", RoomKind.Amphitheatre, 150);
        activityManager.createEvent("PanicCode", 150, LocalDateTime.of(2022, 1, 15, 8, 0), LocalDateTime.of(2022, 1, 30, 15, 0));
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "CodeTime", 50, activityManager.getEventIdList().get(0));
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(240), "Codex", 20, activityManager.getEventIdList().get(0));
        roomManager.bookRoom(roomManager.getListRoomId().get(1), activityManager.getActivitiesId().get(0));
        roomManager.bookRoom(roomManager.getListRoomId().get(0), activityManager.getActivitiesId().get(1));
    }

    @Test
    public void searchFreeRoomAdvancedTest() throws RoomNotFoundException {
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 17, 0), Duration.ofMinutes(120), "Test", 20, activityManager.getEventIdList().get(0));
        UUID id = planning.searchFreeRoom(activityManager.getAnActivity(activityManager.getActivitiesId().get(2)).getBeginning(),
                activityManager.getAnActivity(activityManager.getActivitiesId().get(2)).getDuration(),
                activityManager.getAnActivity(activityManager.getActivitiesId().get(2)).getMaxPeopleAmount());
        assertEquals("Salle 102", inMemoryDatabase.getRooms().get(id).getName());
    }

    @Test
    public void searchFreeRoomAdvancedTestException() {
        RoomNotFoundException thrown = assertThrows(RoomNotFoundException.class, () -> {
            activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "Exception", 50, activityManager.getEventIdList().get(0));
            UUID id = planning.searchFreeRoom(RoomKind.Classroom,
                    activityManager.getAnActivity(activityManager.getActivitiesId().get(2)).getBeginning(),
                    activityManager.getAnActivity(activityManager.getActivitiesId().get(2)).getDuration(),
                    activityManager.getAnActivity(activityManager.getActivitiesId().get(2)).getMaxPeopleAmount());
        });
        assertEquals(RoomNotFoundException.class, thrown.getClass());
    }
}
