package fr.stonksdev.backend.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class RoomPlanningAdvancedTest {

    @Autowired
    private RoomManager roomManager;
    @Autowired
    private StonksEventManager eventManager;
    @Autowired
    private RoomPlanning planning;
    @MockBean
    private Mail mailMock;
    @Autowired
    ActivityManager activityManager;

    private List<Room> rooms;
    private StonksEvent event1;
    private List<Activity> activities;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException, AlreadyExistingEventException, AlreadyExistingActivityException, RoomAlreadyBookedException, RoomNotFoundException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        rooms = new ArrayList<>();
        activities = new ArrayList<>();

        rooms.add(roomManager.create("Salle 101", RoomKind.Classroom, 30));
        rooms.add(roomManager.create("Salle 102", RoomKind.Classroom, 50));
        rooms.add(roomManager.create("AmphiOuest", RoomKind.Amphitheatre, 150));

        event1 = eventManager.createEvent("PanicCode", 150, LocalDateTime.of(2022, 1, 15, 8, 0), LocalDateTime.of(2022, 1, 30, 15, 0));

        activities.add(activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "CodeTime", 50, event1));
        activities.add(activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(240), "Codex", 20, event1));

        roomManager.bookRoom(planning.searchFreeRoom(activities.get(0).getBeginning(), activities.get(0).getDuration(), activities.get(0).getMaxPeopleAmount()), activities.get(0));
        roomManager.bookRoom(planning.searchFreeRoom(activities.get(1).getBeginning(), activities.get(1).getDuration(), activities.get(1).getMaxPeopleAmount()), activities.get(1));
    }

    @Test
    public void searchFreeRoomAdvancedTest() throws AlreadyExistingActivityException, RoomNotFoundException {
        activities.add(activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 17, 0), Duration.ofMinutes(120), "Test", 20, event1));
        Room room2 = planning.searchFreeRoom(activities.get(2).getBeginning(),
                activities.get(2).getDuration(),
                activities.get(2).getMaxPeopleAmount());
        assertEquals("Salle 102", room2.getName());
    }

    @Test
    public void searchFreeRoomAdvancedTestException() {
        RoomNotFoundException thrown = assertThrows(RoomNotFoundException.class, () -> {
            activities.add(activityManager.createActivity(LocalDateTime.of(2022, 1, 15, 15, 0), Duration.ofMinutes(120), "Exception", 160, event1));
            Room room2 = planning.searchFreeRoom(activities.get(2).getBeginning(),
                    activities.get(2).getDuration(),
                    activities.get(2).getMaxPeopleAmount());
        });
        assertEquals(RoomNotFoundException.class, thrown.getClass());
    }
}
