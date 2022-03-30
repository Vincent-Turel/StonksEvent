package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.*;
import fr.stonksdev.backend.interfaces.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class RoomManagerAvancedTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager manager;
    @Autowired
    private StonksEventManager activityManager;
    @Autowired
    private ActivityRegistry activityRegistry;
    @Autowired
    private RoomManager roomManager;

    @MockBean
    Mail mailMock;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException, RoomAlreadyBookedException, ActivityNotFoundException, AlreadyExistingEventException, RoomIdNotFoundException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        manager.reset();
        activityManager.reset();
        manager.create("Salle 103", RoomKind.Classroom, 30);
        activityManager.createEvent("Stonks", 200, LocalDateTime.of(2022, 1, 1, 1, 0), LocalDateTime.of(2022, 1, 1, 5, 0));
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "BonbonDrop", 20, activityManager.getEventIdList().get(0));

        Room room = roomManager.findByName("Salle 103");
        Activity activity = activityRegistry.findByName("BonbonDrop");

        manager.bookRoom(room, activity);
    }

    @Test
    void bookRoomTest() {
        assertEquals(1, inMemoryDatabase.getRoomPlanning().size());
        assertEquals("BonbonDrop", inMemoryDatabase.getActivities().get(inMemoryDatabase.getRoomPlanning().get(manager.getListRoomId().get(0)).get(0)).getName());
    }

    @Test
    void roomAlreadyBookedExceptionTest() throws ActivityNotFoundException, RoomIdNotFoundException {
        Room room = roomManager.findByName("Salle 103");
        Activity activity = activityRegistry.findByName("BonbonDrop");

        RoomAlreadyBookedException thrown = assertThrows(RoomAlreadyBookedException.class, () -> {
            activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "BonbonDropNext", 30, activityManager.getEventIdList().get(0));
            manager.bookRoom(room, activity);
        });
        assertEquals(RoomAlreadyBookedException.class, thrown.getClass());
    }

    @Test
    void freeRoomTest() throws RoomIdNotFoundException, ActivityNotFoundException {
        Room room = roomManager.findByName("Salle 103");
        Activity activity = activityRegistry.findByName("BonbonDrop");

        manager.freeRoom(room, activity);
        assertEquals(0, inMemoryDatabase.getRoomPlanning().get(manager.getListRoomId().get(0)).size());
    }
}
