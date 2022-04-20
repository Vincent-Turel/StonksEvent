package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest()
public class StonksEventManagerTest {
    /*
    REFAIRE LES TEST EN ISOLATION
     */
    @Autowired
    private StonksEventManager manager;
    @Autowired
    private ActivityManager activityManager;

    private List<StonksEvent> eventList;
    private List<Activity> activities;

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
        eventList = new ArrayList<>();
        activities = new ArrayList<>();
        eventList.add(manager.createEvent("StonksEvent", 100, date_1, date_2));
        eventList.add(manager.createEvent("CrazyGame", 50, date_2, date_3));
    }

    @Test
    void createEventTest() {
        assertEquals(2, eventList.size());
        //EVENT 1:
        assertEquals("StonksEvent", eventList.get(0).getName());
        assertEquals(100, eventList.get(0).getAmountOfPeople());
        assertEquals(date_1, eventList.get(0).getStartDate());
        assertEquals(date_2, eventList.get(0).getEndDate());
        //EVENT 2:
        assertEquals("CrazyGame", eventList.get(1).getName());
        assertEquals(50, eventList.get(1).getAmountOfPeople());
        assertEquals(date_2, eventList.get(1).getStartDate());
        assertEquals(date_3, eventList.get(1).getEndDate());
    }

//    @Test
//    void updateEventTest() throws EventNotFoundException {
//        assertEquals("StonksEvent", eventList.get(0).getName());
//        assertEquals(100, eventList.get(0).getAmountOfPeople());
//        assertEquals(date_1, eventList.get(0).getStartDate());
//        assertEquals(date_2, eventList.get(0).getEndDate());
//        manager.updateEvent(eventList.get(0), 50, date_3, date_4);
//        assertEquals("StonksEvent", eventList.get(0).getName());
//        assertEquals(50, eventList.get(0).getAmountOfPeople());
//        assertEquals(date_3, eventList.get(0).getStartDate());
//        assertEquals(date_4, eventList.get(0).getEndDate());
//    }

    /* Passer en IT
    @Test
    void deleteEventTest() throws EventNotFoundException {
        assertEquals(2, manager.getAllEvent().size());
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(1, manager.getAllEvent().size());
        assertEquals("CrazyGame", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
    }*/

    /* Passer en IT
    @Test
    void deleteEventWithActivitiesTest() throws EventNotFoundException {
        assertEquals(2, manager.getAllEvent().size());
        assertEquals("StonksEvent", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
        manager.createActivity(date_2, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(1, manager.getAllActivities().size());
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(1, manager.getAllEvent().size());
        assertEquals(0, manager.getAllActivities().size());
    }*/

    @Test
    void createActivityTest() throws AlreadyExistingActivityException {
        activities.add(activityManager.createActivity(date_2, duration_1, "CodingBattle", 50, eventList.get(0)));
        assertEquals(date_2, activities.get(0).getBeginning());
        assertEquals(duration_1, activities.get(0).getDuration());
        assertEquals(50,activities.get(0).getMaxPeopleAmount());
        assertEquals("CodingBattle", activities.get(0).getName());

        activityManager.createActivity(date_3, duration_1, "CodingGame", 20, eventList.get(0));
        assertEquals(2, activities.size());
        //YOU CAN'T CREATE AN ACTIVITY THAT ALREADY EXIST :
        /*manager.createActivity(date_3, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(2, manager.getAllActivities().size());*/
    }

    /*
    @Test
    void deleteEventWithMoreActivitiesTest() throws AlreadyExistingEventException, EventNotFoundException {
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
    }*/
}
