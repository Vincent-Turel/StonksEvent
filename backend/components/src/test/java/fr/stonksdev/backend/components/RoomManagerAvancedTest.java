package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.io.Console;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class RoomManagerAvancedTest {

    @Autowired
    RoomManager manager;
    @Autowired
    StonksEventManager eventManager;
    @Autowired
    ActivityManager activityManager;
    @MockBean
    Mail mailMock;

    private StonksEvent event1;
    private Room room;
    private boolean isBook;
    private Activity activity;

    @BeforeEach
    void setup() throws AlreadyExistingEventException, AlreadyExistingActivityException, RoomAlreadyBookedException, AlreadyExistingRoomException {
        when(mailMock.send(anyString(),anyString(), anyString())).thenReturn(true);

        room = manager.create("SalleDePpz",RoomKind.Classroom,30);

        event1 = eventManager.createEvent("Stonks",
                200,
                LocalDateTime.of(2022, 1, 1, 1, 0),
                LocalDateTime.of(2022, 1, 1, 5, 0));

        activity = activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0),
                Duration.ofMinutes(120),
                "BonbonDrop",
                20,
                event1);
        isBook = manager.bookRoom(room,
                activity);
    }

    @Test
    void bookRoomTest() throws RoomNotFoundException {
        Planning planning = manager.getPlanningOf(event1,room);
        assertTrue(isBook);
        assertEquals(1, room.getActivities().size());
        assertEquals(1, planning.getSize());
        Activity[] temp = room.getActivities().toArray(new Activity[room.getActivities().size()]);
        assertEquals("BonbonDrop", temp[0].getName());
    }

    @Test
    void roomAlreadyBookedExceptionTest() throws AlreadyExistingActivityException, RoomAlreadyBookedException {
        Activity activity2 = activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "BonbonDropNext", 30, event1);
        boolean itsFalseHere = manager.bookRoom(room, activity2);
        assertFalse(itsFalseHere);
    }

    @Test
    void freeRoomTest() throws RoomNotFoundException {
        Planning planning = manager.getPlanningOf(event1,room);
        assertEquals(1, planning.getSize());
        manager.freeRoom(room, activity);
        Planning planning2 = manager.getPlanningOf(event1,room);
        assertEquals(0, planning2.getSize());
    }
}
