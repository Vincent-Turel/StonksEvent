package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.interfaces.Mail;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

@SpringBootTest()
public class StonksEventManagerTest {
    /*
    REFAIRE LES TEST EN ISOLATION
     */
    @Autowired
    private StonksEventManager manager;

    @MockBean
    private Mail mailMock;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final LocalDateTime date_3 = LocalDateTime.of(2022, 2, 1, 12, 12);
    private final LocalDateTime date_4 = LocalDateTime.of(2022, 2, 5, 13, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);

    @BeforeEach
    void setup() throws AlreadyExistingEventException {
        when(mailMock.send(anyString(),anyString(), anyString())).thenReturn(true);

        manager.reset();
        manager.createEvent("StonksEvent", 100, date_1, date_2);
        manager.createEvent("CrazyGame", 50, date_2, date_3);
    }

    @Test
    void createEventTest() throws EventIdNotFoundException {
        assertEquals(2, manager.getAllEvent().size());
        //EVENT 1:
        assertEquals("StonksEvent", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
        assertEquals(100, manager.getAnEvent(manager.getEventIdList().get(0)).getAmountOfPeople());
        assertEquals(date_1, manager.getAnEvent(manager.getEventIdList().get(0)).getStartDate());
        assertEquals(date_2, manager.getAnEvent(manager.getEventIdList().get(0)).getEndDate());
        //EVENT 2:
        assertEquals("CrazyGame", manager.getAnEvent(manager.getEventIdList().get(1)).getName());
        assertEquals(50, manager.getAnEvent(manager.getEventIdList().get(1)).getAmountOfPeople());
        assertEquals(date_2, manager.getAnEvent(manager.getEventIdList().get(1)).getStartDate());
        assertEquals(date_3, manager.getAnEvent(manager.getEventIdList().get(1)).getEndDate());
    }

    @Test
    void updateEventTest() throws EventIdNotFoundException {
        StonksEvent event_1 = manager.getAnEvent(manager.getEventIdList().get(0));
        StonksEvent event_2 = manager.getAnEvent(manager.getEventIdList().get(1));
        assertEquals("StonksEvent", event_1.getName());
        assertEquals(100, event_1.getAmountOfPeople());
        assertEquals(date_1, event_1.getStartDate());
        assertEquals(date_2, event_1.getEndDate());
        manager.updateEvent(event_1.getId(), event_2.getAmountOfPeople(), event_2.getStartDate(), event_2.getEndDate());
        assertEquals("StonksEvent", event_1.getName());
        assertEquals(event_2.getAmountOfPeople(), event_1.getAmountOfPeople());
        assertEquals(event_2.getStartDate(), event_1.getStartDate());
        assertEquals(event_2.getEndDate(), event_1.getEndDate());
    }

    @Test
    void deleteEventTest() throws EventIdNotFoundException {
        assertEquals(2, manager.getAllEvent().size());
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(1, manager.getAllEvent().size());
        assertEquals("CrazyGame", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
    }

    @Test
    void deleteEventWithActivitiesTest() throws EventIdNotFoundException {
        assertEquals(2, manager.getAllEvent().size());
        assertEquals("StonksEvent", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
        manager.createActivity(date_2, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(1, manager.getAllActivities().size());
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(1, manager.getAllEvent().size());
        assertEquals(0, manager.getAllActivities().size());
    }

    @Test
    void createActivityTest() {
        manager.createActivity(date_2, duration_1, "CodingBattle", "Code competition", 50, manager.getEventIdList().get(0));
        assertEquals(date_2, manager.getAnActivity(manager.getActivitiesId().get(0)).getBeginning());
        assertEquals(duration_1, manager.getAnActivity(manager.getActivitiesId().get(0)).getDuration());
        assertEquals("CodingBattle", manager.getAnActivity(manager.getActivitiesId().get(0)).getName());
        assertEquals("Code competition", manager.getAnActivity(manager.getActivitiesId().get(0)).getDescription());
        assertEquals(1, manager.getAllActivities().size());

        manager.createActivity(date_3, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(2, manager.getAllActivities().size());
        //YOU CAN'T CREATE AN ACTIVITY THAT ALREADY EXIST :
        manager.createActivity(date_3, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(2, manager.getAllActivities().size());
    }

    @Test
    void deleteEventWithMoreActivitiesTest() throws AlreadyExistingEventException, EventIdNotFoundException {
        manager.createEvent("BonbonStyle", 100, date_3, date_4);
        manager.createActivity(date_3, duration_1, "BonbonRedParty!", 20, manager.getEventIdList().get(2));
        manager.createActivity(date_3, duration_1, "BonbonBlueParty!", 50, manager.getEventIdList().get(2));
        manager.createActivity(date_3, duration_1, "CodingGame", manager.getEventIdList().get(0));
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(2, manager.getAllEvent().size());
        assertEquals(2, manager.getAllActivities().size());
        assertEquals("BonbonStyle", manager.getAnEvent(manager.getEventIdList().get(1)).getName());
        assertEquals("BonbonRedParty!", manager.getAnActivity(manager.getActivitiesId().get(0)).getName());
        assertEquals("BonbonBlueParty!", manager.getAnActivity(manager.getActivitiesId().get(1)).getName());
    }
}
