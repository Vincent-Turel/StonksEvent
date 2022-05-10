package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.components.interfaces.TaskGenerator;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.RoomKind;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class FindTaskTest {

    @Autowired
    private TaskGenerator taskGenerator;

    @Autowired
    private StonksEventManager eventManager;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private RoomManager roomManager;

    @MockBean
    Mail mail;

    private final LocalDateTime eventStartDate = LocalDateTime.of(2022, 8, 4, 9, 0, 0);
    private final LocalDateTime eventEndDate = LocalDateTime.of(2022, 8, 5, 19, 0, 0);
    private final LocalDateTime activity1StartDate = LocalDateTime.of(2022, 8, 4, 12, 0, 0);
    private final Duration activity1Duration = Duration.ofMinutes(30);

    @Test
    void doesItWork() throws AlreadyExistingEventException, RoomNotFoundException, ActivityNotFoundException, AlreadyExistingActivityException, AlreadyExistingRoomException {
        var event = eventManager.createEvent("RustConf 2022", 150, eventStartDate, eventEndDate);
        activityManager.createActivity(activity1StartDate, activity1Duration, "Sasha's Talk", 25, event);
        roomManager.create("Salle O+318", RoomKind.Classroom, 35);

        var tasks = taskGenerator.tasksForEvent(event);

        assertEquals(2, tasks.size());
    }
}
