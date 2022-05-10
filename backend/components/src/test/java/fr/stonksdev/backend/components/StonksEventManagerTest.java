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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest()
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class StonksEventManagerTest {
    @Autowired
    private StonksEventManager manager;

    @Autowired
    ActivityManager activityManager;

    @MockBean
    private Mail mailMock;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final LocalDateTime date_3 = LocalDateTime.of(2023, 2, 1, 12, 12);
    private final LocalDateTime date_4 = LocalDateTime.of(2023, 2, 1, 12, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);

    @BeforeEach
    void setup() throws AlreadyExistingEventException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        manager.createEvent("StonksEvent", 100, date_1, date_2);
        manager.createEvent("CrazyGame", 50, date_2, date_3);
    }

    @Test
    void createEventTest() {
        var eventOpt = manager.findByName("StonksEvent");
        assertTrue(eventOpt.isPresent());
        var event = eventOpt.get();
        assertEquals(100, event.getAmountOfPeople());
        assertEquals(date_1, event.getStartDate());
        assertEquals(date_2, event.getEndDate());

        var eventOpt2 = manager.findByName("CrazyGame");
        assertTrue(eventOpt2.isPresent());
        var event2 = eventOpt2.get();
        assertEquals(50, event2.getAmountOfPeople());
        assertEquals(date_2, event2.getStartDate());
        assertEquals(date_3, event2.getEndDate());
    }

    @Test
    void updateEventTest() throws EventNotFoundException {
        var event = manager.findByName("StonksEvent").get();
        assertEquals("StonksEvent", event.getName());
        assertEquals(100, event.getAmountOfPeople());
        assertEquals(date_1, event.getStartDate());
        assertEquals(date_2, event.getEndDate());
        var updatedEvent = manager.updateEvent(event.getId(), 50, date_3, date_4);
        assertEquals("StonksEvent", updatedEvent.getName());
        assertEquals(50, updatedEvent.getAmountOfPeople());
        assertEquals(date_3, updatedEvent.getStartDate());
        assertEquals(date_4, updatedEvent.getEndDate());
    }

    @Test
    void deleteEventTest() {
        manager.deleteEvent(manager.findByName("StonksEvent").get());
        assertTrue(manager.findByName("StonksEvent").isEmpty());
    }

    @Test
    void deleteEventWithMoreActivitiesTest() throws AlreadyExistingEventException, AlreadyExistingActivityException {
        var event = manager.createEvent("BonbonStyle", 100, date_3, date_4);
        var act1 = activityManager.createActivity(date_3, duration_1, "BonbonRedParty!", 20, event);
        var act2 = activityManager.createActivity(date_3, duration_1, "BonbonBlueParty!", 50, event);
        var act3 = activityManager.createActivity(date_3, duration_1, "CodingGame", event);
        var act4 = activityManager.createActivity(date_3, duration_1, "CodingGame2", manager.findByName("StonksEvent").get());

        assertTrue(activityManager.findById(act1.getId()).isPresent());
        assertTrue(activityManager.findById(act2.getId()).isPresent());
        assertTrue(activityManager.findById(act3.getId()).isPresent());
        assertTrue(activityManager.findById(act4.getId()).isPresent());

        manager.deleteEvent(event);

        assertTrue(activityManager.findById(act1.getId()).isEmpty());
        assertTrue(activityManager.findById(act2.getId()).isEmpty());
        assertTrue(activityManager.findById(act3.getId()).isEmpty());
        assertTrue(activityManager.findById(act4.getId()).isPresent());
    }
}
