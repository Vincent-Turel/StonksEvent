package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.interfaces.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StonksEventManagerActivityTest {
    /*
    REFAIRE LES TEST EN ISOLATION
     */
    @Autowired
    private StonksEventManager manager;

    @MockBean
    private Mail mailMock;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);
    private final Duration duration_2 = Duration.ofMinutes(100);

    @BeforeEach
    void setup() throws AlreadyExistingEventException {
        when(mailMock.send(anyString(),anyString(), anyString())).thenReturn(true);

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
    void updateActivityTest() throws ActivityNotFoundException {
        Activity activity = manager.getAnActivity(manager.getActivitiesId().get(0));
        manager.updateActivity(activity, 200, date_2, duration_2);
        assertEquals("MyLittlePony", activity.getName());
        assertEquals(200, activity.getMaxPeopleAmount());
        assertEquals(date_2, activity.getBeginning());
        assertEquals(duration_2, activity.getDuration());
    }

    @Test
    void deleteActivityTest() {
        manager.deleteActivity(manager.getAllActivities().values().stream().findFirst().get());

        assertEquals(0, manager.getAllActivities().size());
    }
}
