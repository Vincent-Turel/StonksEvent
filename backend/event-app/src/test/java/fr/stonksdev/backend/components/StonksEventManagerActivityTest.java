package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

@SpringBootTest
public class StonksEventManagerActivityTest {
    /*
    REFAIRE LES TEST EN ISOLATION
     */
    @Autowired
    private StonksEventManager manager;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);
    private final Duration duration_2 = Duration.ofMinutes(100);

    @BeforeEach
    void setup() {
        manager.reset();
        manager.createEvent("ActivityEvent", 150, date_1, date_2);
        manager.createActivity(date_1, duration_1, "MyLittlePony", "Pony are love, pony are life", 50, manager.getEventIdList().get(0));
    }

    @Test
    void createActivityTest() {
        assertEquals(date_1, manager.getAnActivity(manager.getActivitiesId().get(0)).getBeginning());
        assertEquals(duration_1, manager.getAnActivity(manager.getActivitiesId().get(0)).getDuration());
        assertEquals("MyLittlePony", manager.getAnActivity(manager.getActivitiesId().get(0)).getName());
        assertEquals("Pony are love, pony are life", manager.getAnActivity(manager.getActivitiesId().get(0)).getDescription());
        assertEquals(50, manager.getAnActivity(manager.getActivitiesId().get(0)).getMaxPeopleAmount());
        assertEquals(manager.getEventIdList().get(0), manager.getAnActivity(manager.getActivitiesId().get(0)).getEventId());
    }

    @Test
    void setNewActivityName() {
        manager.setNewActivityName("NoelStonks", manager.getActivitiesId().get(0));
        assertEquals("NoelStonks", manager.getAnActivity(manager.getActivitiesId().get(0)).getName());
    }

    @Test
    void setNewActivityBeginningTest() {
        manager.setNewActivityBeginning(date_2, manager.getActivitiesId().get(0));
        assertEquals(date_2, manager.getAnActivity(manager.getActivitiesId().get(0)).getBeginning());
    }

    @Test
    void setNewActivityDurationTest() {
        manager.setNewActivityDuration(duration_2, manager.getActivitiesId().get(0));
        assertEquals(duration_2, manager.getAnActivity(manager.getActivitiesId().get(0)).getDuration());
    }

    @Test
    void setNewActivityDescriptionTest() {
        manager.setNewActivityDescription("NewDescription", manager.getActivitiesId().get(0));
        assertEquals("NewDescription", manager.getAnActivity(manager.getActivitiesId().get(0)).getDescription());
    }

    @Test
    void setNewActivityMaxPeopleAmount() {
        manager.setNewActivityMaxPeopleAmount(20, manager.getActivitiesId().get(0));
        assertEquals(20, manager.getAnActivity(manager.getActivitiesId().get(0)).getMaxPeopleAmount());
    }

    @Test
    void deleteActivityTest() {
        manager.deleteActivity(manager.getActivitiesId().get(0));
        assertEquals(0, manager.getAllActivities().size());
    }
}
