package fr.stonksdev.backend.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest()
public class RoomPlanningAdvancedTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private StonksEventManager activityManager;
    @Autowired
    private RoomPlanning planning;
    @Autowired
    private ActivityRegistry activityRegistry;

    @MockBean
    private Mail mailMock;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException, RoomAlreadyBookedException, ActivityNotFoundException, AlreadyExistingEventException, RoomIdNotFoundException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        activityManager.reset();
        roomManager.reset();
        roomManager.create("Salle 101", RoomKind.Classroom, 30);
        roomManager.create("Salle 102", RoomKind.Classroom, 50);
        roomManager.create("AmphiOuest", RoomKind.Amphitheatre, 150);
        activityManager.createEvent("PanicCode", 150, LocalDateTime.of(2022, 1, 15, 8, 0), LocalDateTime.of(2022, 1, 30, 15, 0));
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "CodeTime", 50, activityManager.getEventIdList().get(0));
        activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(240), "Codex", 20, activityManager.getEventIdList().get(0));

        Room room1 = roomManager.findByName("Salle 102");
        Room room2 = roomManager.findByName("Salle 101");

        Activity act1 = activityRegistry.findByName("CodeTime");
        Activity act2 = activityRegistry.findByName("Codex");

        roomManager.bookRoom(room1, act1);
        roomManager.bookRoom(room2, act2);
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
