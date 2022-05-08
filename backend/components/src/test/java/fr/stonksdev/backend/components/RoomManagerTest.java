package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.components.repositories.ActivityRepository;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@Transactional
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
public class RoomManagerTest {

    @Autowired
    RoomManager manager;

    @Autowired
    ActivityRepository activityRepo;

    @MockBean
    Mail mail;

    private Room room;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException {
        room = manager.create("Salle 103", RoomKind.Classroom, 30);
    }

    @Test
    void createTest() throws AlreadyExistingRoomException {
        manager.create("Salle 104", RoomKind.Classroom, 25);
        manager.create("AmphiStonks", RoomKind.Amphitheatre, 150);
        Room room = manager.findByName("Salle 103").get();
        Room room2 = manager.findByName("Salle 104").get();
        Room room3 = manager.findByName("AmphiStonks").get();
        assertTrue(true);
    }

    @Test
    void AlreadyExistingRoomExceptionTest() {
        AlreadyExistingRoomException thrown = assertThrows(AlreadyExistingRoomException.class, () -> manager.create("Salle 103", RoomKind.Amphitheatre, 50));
        assertEquals(AlreadyExistingRoomException.class, thrown.getClass());
    }

    @Test
    void modifyTest() throws RoomNotFoundException {
        Room newRoom = new Room("Salle 103", RoomKind.Meeting, 50);
        Room newRoomUpdated = manager.modify(room.getId(), newRoom);
        assertEquals(room.getId(), newRoomUpdated.getId());
        assertEquals("Salle 103", newRoomUpdated.getName());
        assertEquals(RoomKind.Meeting, newRoomUpdated.getRoomKind());
        assertEquals(50, newRoomUpdated.getCapacity());
    }

    @Test
    void RoomNotFoundExceptionTestModify() {
        RoomNotFoundException thrown = assertThrows(RoomNotFoundException.class, () -> {
            Room room = new Room("test", RoomKind.Classroom, 20);
            manager.modify(Long.MAX_VALUE, room);
        });
        assertEquals(RoomNotFoundException.class, thrown.getClass());
    }

    @Test
    void deleteTest() {
        manager.delete(room);
        assertTrue(manager.findById(room.getId()).isEmpty());
    }
}
