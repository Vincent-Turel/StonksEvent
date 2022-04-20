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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ActivityManager activityManager;
    private List<StonksEvent> eventList;
    private Activity activity;

    @MockBean
    private Mail mailMock;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 1, 12, 12);
    private final LocalDateTime date_2 = LocalDateTime.of(2022, 1, 5, 13, 12);
    private final Duration duration_1 = Duration.ofMinutes(120);
    private final Duration duration_2 = Duration.ofMinutes(100);

    public StonksEventManagerActivityTest() {
    }

    @BeforeEach
    void setup() throws AlreadyExistingEventException, AlreadyExistingActivityException {
        when(mailMock.send(anyString(),anyString(), anyString())).thenReturn(true);

        eventList = new ArrayList<>();
        eventList.add(manager.createEvent("ActivityEvent", 150, date_1, date_2));
        activity = activityManager.createActivity(date_1, duration_1, "MyLittlePony", 50, eventList.get(0));
    }

    @Test
    void createActivityTest() {
        assertEquals(date_1, activity.getBeginning());
        assertEquals(duration_1, activity.getDuration());
        assertEquals("MyLittlePony", activity.getName());
        assertEquals(50, activity.getMaxPeopleAmount());
        /*assertEquals(eventList.get(0).getName(), activity.get); -> add a getter*/
    }

    @Test
    void updateActivityTest() throws AlreadyExistingActivityException {
        Activity change = activityManager.createActivity( date_2,duration_2,activity.getName(),200,eventList.get(0));
        assertEquals("MyLittlePony", change.getName());
        assertEquals(200, change.getMaxPeopleAmount());
        assertEquals(date_2, change.getBeginning());
        assertEquals(duration_2, change.getDuration());
    }

    /* Passer en IT
    @Test
    void deleteActivityTest() {
        manager.deleteActivity(manager.getActivitiesId().get(0));
        assertEquals(0, manager.getAllActivities().size());
    }*/
}
