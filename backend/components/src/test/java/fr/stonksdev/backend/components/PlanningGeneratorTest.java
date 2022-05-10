package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.components.repositories.ActivityRepository;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class PlanningGeneratorTest {

    @Autowired
    RoomManager roomManager;

    @Autowired
    StonksEventManager eventManager;

    @Autowired
    ActivityManager activityManager;

    @Autowired
    ActivityRepository activityRepository;

    @MockBean
    Mail mailMock;

    private StonksEvent event1;
    private StonksEvent event2;
    private List<Activity> availableActivities;

    @BeforeEach
    void setup() throws Exception {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);

        availableActivities = new ArrayList<>();

        roomManager.create("O+301", RoomKind.Meeting, 30);
        roomManager.create("O+104", RoomKind.Classroom, 15);
        roomManager.create("O+302", RoomKind.Classroom, 34);
        roomManager.create("O+303", RoomKind.Classroom, 45);

        // This one will have two activities
        event1 = eventManager.createEvent(
                "Fin d'année",
                150,
                LocalDateTime.of(2022, 1, 1, 18, 0, 0),
                LocalDateTime.of(2022, 1, 2, 10, 0, 0)
        );

        // This one won't
        event2 = eventManager.createEvent(
                "atelier couture avec Bébert",
                150,
                LocalDateTime.of(2022, 1, 1, 18, 0, 0),
                LocalDateTime.of(2022, 1, 2, 10, 0, 0)
        );

        availableActivities.add(activityManager.createActivity(
                LocalDateTime.of(2022, 1, 1, 18, 0, 0),
                Duration.ofMinutes(120),
                "Discours de M. Papazian (1)",
                20,
                event1
        ));
        availableActivities.add(activityManager.createActivity(
                LocalDateTime.of(2022, 1, 1, 20, 0, 0),
                Duration.ofMinutes(120), "Discours de M. Papazian (2)",
                20,
                event1
        ));
    }

    @Test
    void getPlanningTwiceReturnsSame() throws Exception {
        Planning planning = roomManager.getPlanningOf(event1);
        Planning planning2 = roomManager.getPlanningOf(event1);
        assertEquals(planning.getSize(), planning2.getSize());
        assertEquals(planning.getTimeSlots().get(0).getActivity(), planning2.getTimeSlots().get(0).getActivity());
    }

    @Test
    void eventWithNoActivityHasNoPlanning() throws Exception {
        Planning planning = roomManager.getPlanningOf(event2);
        long amountOfActivities = planning.getSize();
        assertEquals(0, amountOfActivities);
    }

    @Test
    void eventWithActivitiesHasSomePlanning() throws Exception {
        Planning planning = roomManager.getPlanningOf(event1);
        long amountOfActivities = planning.getSize();
        assertEquals(amountOfActivities, 2);
    }

    @Test
    void activityNameMatches() throws Exception {
        Stream<String> activities = roomManager.getPlanningOf(event1).getTimeSlots().stream().map(timeSlot -> timeSlot.getActivity().getName());
        List<String> allName = availableActivities.stream().map(Activity::getName).collect(Collectors.toList());
        activities.forEach(name -> assertTrue(allName.contains(name)));
    }
}
