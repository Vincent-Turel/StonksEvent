package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.TimeSlot;
import fr.stonksdev.backend.interfaces.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlanningGeneratorTest {
    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private StonksEventManager eventManager;

    @MockBean
    Mail mailMock;

    static final List<String> availableActivities = List.of("Discours de M. Papazian (1)", "Discours de M. Papazian (2)");

    @BeforeEach
    void setup() throws Exception {
        when(mailMock.send(anyString(),anyString(), anyString())).thenReturn(true);

        inMemoryDatabase.flush();
        roomManager.reset();

        roomManager.create("O+301", RoomKind.Meeting, 30);
        roomManager.create("O+104", RoomKind.Classroom, 15);
        roomManager.create("O+302", RoomKind.Classroom, 34);

        // This one will have two activities
        StonksEvent event1 = eventManager.createEvent(
                "La Nuit de l'Info",
                150,
                LocalDateTime.of(2022, 1, 1, 18, 0, 0),
                LocalDateTime.of(2022, 1, 2, 10, 0, 0)
        );

        // This one won't
        StonksEvent event2 = eventManager.createEvent(
                "atelier couture avec Bébert",
                150,
                LocalDateTime.of(2022, 1, 1, 18, 0, 0),
                LocalDateTime.of(2022, 1, 2, 10, 0, 0)
        );

        eventManager.createActivity(LocalDateTime.of(2022, 1, 1, 18, 0, 0), Duration.ofMinutes(120), "Discours de M. Papazian (1)", "Le directeur du département informatique fera ses adieux à ses fans (1)", 20, event1.getId());
        eventManager.createActivity(LocalDateTime.of(2022, 1, 1, 20, 0, 0), Duration.ofMinutes(120), "Discours de M. Papazian (2)", "Le directeur du département informatique fera ses adieux à ses fanss (4)", 20, event1.getId());
    }

    @Test
    void eventWithNoActivityHasNoPlanning() throws Exception {
        StonksEvent event = eventManager.findByName("atelier couture avec Bébert");
        Map<String, List<TimeSlot>> planning = roomManager.getPlanningOf(event);
        assertEquals(planning.size(), 0);
    }

    @Test
    void eventWithActivitiesHasSomePlanning() throws Exception {
        StonksEvent event = eventManager.findByName("La Nuit de l'Info");
        Map<String, List<TimeSlot>> planning = roomManager.getPlanningOf(event);
        long amountOfActivities = planning.values().stream().mapToLong(Collection::size).sum();
        assertEquals(amountOfActivities, 2);
    }

    @Test
    void activityNameMatches() throws Exception {
        StonksEvent event = eventManager.findByName("La Nuit de l'Info");

        Stream<String> activities = roomManager.getPlanningOf(event).values().stream().flatMap(Collection::stream).map(activity -> activity.activityName);

        activities.forEach(name -> assertTrue(availableActivities.contains(name)));
    }
}
