package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StonksEventManagerTest {

    @Autowired
    private StonksEventManager manager;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final LocalDateTime date_3 = LocalDateTime.of(2022, 2, 1, 12, 12);
    private final LocalDateTime date_4 = LocalDateTime.of(2022, 2, 5, 13, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);

    @Test
    @Order(1)
    void createEventTest() {
        manager.createEvent("StonksEvent", 100, date_1, date_2);
        assertEquals(1, manager.getNbEvent());
        manager.createEvent("CrazyGame", 50, date_2, date_3);
        assertEquals(2, manager.getNbEvent());

        StonksEvent event_1 = manager.getAnEvent(manager.getEventIdList().get(0));
        assertEquals("StonksEvent", event_1.getName());
        assertEquals(100, event_1.getAmountOfPeople());
        assertEquals(date_1, event_1.getStartDate());
        assertEquals(date_2, event_1.getEndDate());

        StonksEvent event_2 = manager.getAnEvent(manager.getEventIdList().get(1));
        assertEquals("CrazyGame", event_2.getName());
        assertEquals(50, event_2.getAmountOfPeople());
        assertEquals(date_2, event_2.getStartDate());
        assertEquals(date_3, event_2.getEndDate());
    }

    @Test
    @Order(2)
    void setNewEventNameTest() {
        assertEquals("StonksEvent", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
        assertEquals("CrazyGame", manager.getAnEvent(manager.getEventIdList().get(1)).getName());
        manager.setNewEventName("MagicalFurry", manager.getEventIdList().get(0));
        manager.setNewEventName("JapanExpo", manager.getEventIdList().get(1));
        assertEquals("MagicalFurry", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
        assertEquals("JapanExpo", manager.getAnEvent(manager.getEventIdList().get(1)).getName());
    }

    @Test
    @Order(3)
    void setNewAmountTest() {
        assertEquals(100, manager.getAnEvent(manager.getEventIdList().get(0)).getAmountOfPeople());
        assertEquals(50, manager.getAnEvent(manager.getEventIdList().get(1)).getAmountOfPeople());
        manager.setNewAmount(150, manager.getEventIdList().get(0));
        manager.setNewAmount(300, manager.getEventIdList().get(1));
        assertEquals(150, manager.getAnEvent(manager.getEventIdList().get(0)).getAmountOfPeople());
        assertEquals(300, manager.getAnEvent(manager.getEventIdList().get(1)).getAmountOfPeople());
    }

    @Test
    @Order(4)
    void deleteEventTest() {
        assertEquals(2, manager.getNbEvent());
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(1, manager.getNbEvent());
        assertEquals("JapanExpo", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
    }

    @Test
    @Order(5)
    void deleteEventWithActivitiesTest() {
        assertEquals(1, manager.getNbEvent());
        manager.createEvent("StonksEvent", 100, date_1, date_2);
        assertEquals(2, manager.getNbEvent());
        assertEquals("StonksEvent", manager.getAnEvent(manager.getEventIdList().get(1)).getName());
        manager.createActivity(date_2, duration_1, "CodingGame", manager.getEventIdList().get(1));
        assertEquals(1, manager.getNbActivity());
        manager.deleteEvent(manager.getEventIdList().get(1));
        assertEquals(1, manager.getNbEvent());
        assertEquals(0, manager.getNbActivity());
    }

    @Test
    @Order(6)
    void createActivityTest() {
        manager.createActivity(date_2, duration_1, "CodingBattle", "Code competition", 50, manager.getEventIdList().get(0));
        assertEquals(date_2, manager.getAnActivity(manager.getEventIdList().get(0), manager.getActivitiesName().get(0)).getBeginning());
        assertEquals(duration_1, manager.getAnActivity(manager.getEventIdList().get(0), manager.getActivitiesName().get(0)).getDuration());
        assertEquals("CodingBattle", manager.getAnActivity(manager.getEventIdList().get(0), manager.getActivitiesName().get(0)).getName());
        assertEquals("Code competition", manager.getAnActivity(manager.getEventIdList().get(0), manager.getActivitiesName().get(0)).getDescription());
        assertEquals(1, manager.getNbActivity());

        manager.createActivity(date_3, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(2, manager.getNbActivity());
        //YOU CAN'T CREATE AN ACTIVITY THAT ALREADY EXIST :
        manager.createActivity(date_3, duration_1, "CodingGame", manager.getEventIdList().get(0));
        assertEquals(2, manager.getNbActivity());
    }

    @Test
    @Order(7)
    void deleteEventWithMoreActivitiesTest() {
        manager.createEvent("BonbonStyle", 100, date_3, date_4);
        manager.createActivity(date_3, duration_1, "BonbonRedParty!", 20, manager.getEventIdList().get(1));
        manager.deleteEvent(manager.getEventIdList().get(0));
        assertEquals(1, manager.getNbEvent());
        assertEquals(1, manager.getNbActivity());
        assertEquals("BonbonStyle", manager.getAnEvent(manager.getEventIdList().get(0)).getName());
        assertEquals("BonbonRedParty!", manager.getAnActivity(manager.getEventIdList().get(0), manager.getActivitiesName().get(0)).getName());
    }

}
