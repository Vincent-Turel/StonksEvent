package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class ActivityManagerTest {
    @Autowired
    private StonksEventManager manager;
    @Autowired
    private ActivityManager activityManager;

    private StonksEvent event;
    private Activity activity;

    @MockBean
    private Mail mailMock;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);
    private final Duration duration_2 = Duration.ofMinutes(100);

    public ActivityManagerTest() {
    }

    @BeforeEach
    void setup() throws AlreadyExistingEventException, AlreadyExistingActivityException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        event = manager.createEvent("ActivityEvent", 150, date_1, date_2);
        activity = activityManager.createActivity(date_1, duration_1, "MyLittlePony", 50, event);
    }

    @Test
    void createActivityTest() {
        assertEquals(date_1, activity.getBeginning());
        assertEquals(duration_1, activity.getDuration());
        assertEquals("MyLittlePony", activity.getName());
        assertEquals(50, activity.getMaxPeopleAmount());
        assertEquals(event.getName(), activity.getEvent().getName());
    }

    @Test
    void updateActivityTest() throws AlreadyExistingActivityException {
        activity.setName("Blahaj");
        activity.setMaxPeopleAmount(200);
        activity.setBeginning(date_2);
        activity.setDuration(duration_2);
        assertEquals("Blahaj", activity.getName());
        assertEquals(200, activity.getMaxPeopleAmount());
        assertEquals(date_2, activity.getBeginning());
        assertEquals(duration_2, activity.getDuration());
    }

    @Test
    void deleteActivityTest() {
        activityManager.deleteActivity(activity);
        assertTrue(manager.findById(activity.getId()).isEmpty());
    }
}
