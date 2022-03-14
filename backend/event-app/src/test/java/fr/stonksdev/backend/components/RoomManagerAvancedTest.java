package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RoomManagerAvancedTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager manager;
    @Autowired
    private StonksEventManager activityManager;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException, RoomAlreadyBookedException, ActivityNotFoundException {
        manager.reset();
        activityManager.reset();
        manager.create("Salle 103", RoomKind.Classroom, 30);
        activityManager.createEvent("Stonks", 200, LocalDateTime.of(2022, 1, 1, 1, 0), LocalDateTime.of(2022, 1, 1, 5, 0));
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "BonbonDrop", 20, activityManager.getEventIdList().get(0));
        manager.bookRoom(manager.getListRoomId().get(0), activityManager.getActivitiesId().get(0));
    }

    @Test
    void bookRoomTest() throws RoomAlreadyBookedException, ActivityNotFoundException {
        assertEquals(1, inMemoryDatabase.getRoomPlanning().size());
        assertEquals("BonbonDrop", inMemoryDatabase.getActivities().get(inMemoryDatabase.getRoomPlanning().get(manager.getListRoomId().get(0)).get(0)).getName());
    }

    @Test
    void roomAlreadyBookedExceptionTest() {
        RoomAlreadyBookedException thrown = assertThrows(RoomAlreadyBookedException.class, () -> {
            activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "BonbonDropNext", 30, activityManager.getEventIdList().get(0));
            manager.bookRoom(manager.getListRoomId().get(0), activityManager.getActivitiesId().get(1));
        });
        assertEquals(RoomAlreadyBookedException.class, thrown.getClass());
    }

    @Test
    void freeRoomTest() throws RoomIdNotFoundException {
        manager.freeRoom(manager.getListRoomId().get(0), activityManager.getActivitiesId().get(0));
        assertEquals(0, inMemoryDatabase.getRoomPlanning().get(manager.getListRoomId().get(0)).size());
    }
}
