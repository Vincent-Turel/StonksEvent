package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@SpringBootTest
@EntityScan(basePackages = {"fr.stonksdev.backend.entities"})
@Transactional
public class RoomPlanningTest {

    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomPlanning planning;
    @MockBean
    private Mail mailMock;

    private final LocalDateTime date_1 = LocalDateTime.of(2022, 1, 31, 15, 0);

    @BeforeEach
    void setup() throws AlreadyExistingRoomException, AlreadyExistingEventException {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);
        roomManager.create("Salle1", RoomKind.Meeting, 25);
        roomManager.create("Salle2", RoomKind.Meeting, 75);
        roomManager.create("Salle3", RoomKind.Amphitheatre, 150);
    }

    @Test
    void searchFreeRoomTestWithoutKind() throws RoomNotFoundException {
        Room find = planning.searchFreeRoom(date_1, Duration.ofMinutes(150), 50);
        assertEquals("Salle2", find.getName());
        find = planning.searchFreeRoom(date_1, Duration.ofMinutes(150), 130);
        assertEquals("Salle3", find.getName());
    }

    @Test
    void searchFreeRoomTestWithKind() throws RoomNotFoundException {
        Room find = planning.searchFreeRoom(RoomKind.Amphitheatre, date_1, Duration.ofMinutes(150), 30);
        assertEquals("Salle3", find.getName());
        find = planning.searchFreeRoom(RoomKind.Amphitheatre, date_1, Duration.ofMinutes(150), 75);
        assertEquals("Salle3", find.getName());
    }

    @Test
    void searchFreeRoomTestException() {
        RoomNotFoundException thrown = assertThrows(RoomNotFoundException.class, () -> {
            planning.searchFreeRoom(RoomKind.Meeting, date_1, Duration.ofMinutes(150), 150);
        });
        assertEquals(RoomNotFoundException.class, thrown.getClass());
    }

    @Test
    void searchFreeRoomTestWithKindAndBestSize() throws RoomNotFoundException, AlreadyExistingRoomException {
        roomManager.create("Salle4", RoomKind.Amphitheatre, 10);
        Room find = planning.searchFreeRoom(RoomKind.Amphitheatre, date_1, Duration.ofMinutes(150), 8);
        assertEquals("Salle4", find.getName());
        roomManager.create("Salle5", RoomKind.Meeting, 150);
        find = planning.searchFreeRoom(RoomKind.Meeting, date_1, Duration.ofMinutes(150), 150);
        assertEquals("Salle5", find.getName());
    }

    @Test
    void searchRoomTest() {
        Room find = roomManager.findByName("Salle1").get();
        assertEquals(find.getCapacity(), 25);
        find = roomManager.findByName("Salle3").get();
        assertEquals(find.getCapacity(), 150);
    }

    @Test
    void searchRoomTestException() {
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            roomManager.findByName("Salle4").get();
        });
        assertEquals(NoSuchElementException.class, thrown.getClass());
    }
}
