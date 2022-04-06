package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.components.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class RoomManagerTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager manager;
    @MockBean
    Mail mailMock;


    @BeforeEach
    void setup() throws AlreadyExistingRoomException {
        when(mailMock.send(anyString(),anyString(), anyString())).thenReturn(true);

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
        AlreadyExistingRoomException thrown = assertThrows(AlreadyExistingRoomException.class, () ->
                manager.create("Salle 103", RoomKind.Amphitheatre, 50)
        );
        assertEquals(AlreadyExistingRoomException.class, thrown.getClass());
    }

    @Test
    void modifyTest() throws RoomIdNotFoundException {
        Room room = manager.findByName("Salle 103");
        Room newRoom = new Room("Salle 104", RoomKind.Meeting, 50);

        manager.modify(room, newRoom);
        assertEquals(manager.getListRoomId().get(0), inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getId());
        assertEquals("Salle 104", inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getName());
        assertEquals(50, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getCapacity());
        assertEquals(RoomKind.Meeting, inMemoryDatabase.getRooms().get(manager.getListRoomId().get(0)).getRoomKind());
    }

    @Test
    void RoomIdNotFoundExceptionTestModify() {
        RoomIdNotFoundException thrown = assertThrows(RoomIdNotFoundException.class, () -> {
            Room room = new Room("I don't exist", RoomKind.Amphitheatre, 200000);

            Room newRoom = new Room("test", RoomKind.Classroom, 20);
            manager.modify(room, newRoom);
        });
        assertEquals(RoomIdNotFoundException.class, thrown.getClass());
    }

    @Test
    void deleteTest() throws RoomIdNotFoundException {
        Room room = manager.findByName("Salle 103");

        manager.delete(room);
        assertEquals(0, manager.getListRoomId().size());
        assertEquals(0, inMemoryDatabase.getRooms().size());
    }

    @Test
    void RoomIdNotFoundExceptionTestDelete() {
        Room fakeRoom = new Room("I do not exist", RoomKind.Meeting, 1);

        RoomIdNotFoundException thrown = assertThrows(RoomIdNotFoundException.class, () -> manager.delete(fakeRoom));
        assertEquals(RoomIdNotFoundException.class, thrown.getClass());
    }
}
