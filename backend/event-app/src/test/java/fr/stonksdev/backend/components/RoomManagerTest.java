package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
public class RoomManagerTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager manager;

    @BeforeEach
    void setup() throws AlreadyExistingRoomException {
        manager.reset();
        manager.create("Salle 103", RoomKind.Classroom, 30);
    }

    @Test
    void createTest() throws AlreadyExistingRoomException {
        manager.reset();
        manager.create("Salle 103", RoomKind.Classroom, 30);
        manager.create("Salle 104", RoomKind.Classroom, 25);
        manager.create("AmphiStonks", RoomKind.Amphitheatre, 150);
        assertEquals("Salle 103", inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getName());
        assertEquals(30, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getCapacity());
        assertEquals("Salle 104", inMemoryDatabase.getRooms().get(manager.getListRoomId().get(1)).getName());
        assertEquals(25, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(1)).getCapacity());
        assertEquals("AmphiStonks", inMemoryDatabase.getRooms().get(manager.getListRoomId().get(2)).getName());
        assertEquals(150, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(2)).getCapacity());
        assertEquals(RoomKind.Amphitheatre, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(2)).getRoomKind());
    }

    @Test
    void AlreadyExistingRoomExceptionTest() {
        AlreadyExistingRoomException thrown = assertThrows(AlreadyExistingRoomException.class, () -> {
            manager.create("Salle 103", RoomKind.Amphitheatre, 50);
        });
        assertEquals(AlreadyExistingRoomException.class, thrown.getClass());
    }

    @Test
    void modifyTest() throws RoomIdNotFoundException {
        Room room = new Room("Salle 104", RoomKind.Meeting, 50);
        manager.modify(manager.getListRoomId().get(0), room);
        assertEquals(manager.getListRoomId().get(0), inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getId());
        assertEquals("Salle 104", inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getName());
        assertEquals(50, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getCapacity());
        assertEquals(RoomKind.Meeting, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getRoomKind());
    }

    @Test
    void RoomIdNotFoundExceptionTestModify() {
        RoomIdNotFoundException thrown = assertThrows(RoomIdNotFoundException.class, () -> {
            Room room = new Room("test", RoomKind.Classroom, 20);
            manager.modify(new UUID(1, 1), room);
        });
        assertEquals(RoomIdNotFoundException.class, thrown.getClass());
    }

    @Test
    void deleteTest() throws RoomIdNotFoundException {
        manager.delete(manager.getListRoomId().get(0));
        assertEquals(0, manager.getListRoomId().size());
        assertEquals(0, inMemoryDatabase.getRooms().size());
    }

    @Test
    void RoomIdNotFoundExceptionTestDelete() {
        RoomIdNotFoundException thrown = assertThrows(RoomIdNotFoundException.class, () -> {
            manager.delete(new UUID(1, 1));
        });
        assertEquals(RoomIdNotFoundException.class, thrown.getClass());
    }
}
