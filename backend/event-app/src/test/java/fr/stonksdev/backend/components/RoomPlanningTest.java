package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

@SpringBootTest
public class RoomPlanningTest {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    @Autowired
    private RoomManager manager;
    @Autowired
    private RoomPlanning planning;
    private final LocalDateTime date_1 = LocalDateTime.of(2022,1,31,15,0);

    @BeforeEach
    void setup() throws AlreadyExistingRoomException {
        manager.reset();
        manager.create("Salle1", RoomKind.Meeting,25);
        manager.create("Salle2", RoomKind.Meeting,75);
        manager.create("Salle3", RoomKind.Amphitheatre,150);
    }

    @Test
    void searchFreeRoomTestWithoutKind() throws RoomNotFoundException {
        Room find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(date_1, Duration.ofMinutes(150),50));
        assertEquals("Salle2",find.getName());
        find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(date_1, Duration.ofMinutes(150),130));
        assertEquals("Salle3",find.getName());
    }

    @Test
    void searchFreeRoomTestWithKind() throws RoomNotFoundException {
        Room find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(RoomKind.Amphitheatre ,date_1, Duration.ofMinutes(150),30));
        assertEquals("Salle3",find.getName());
        find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(RoomKind.Amphitheatre ,date_1, Duration.ofMinutes(150),75));
        assertEquals("Salle3",find.getName());
    }

    @Test
    void searchFreeRoomTestException(){
        RoomNotFoundException thrown = assertThrows(RoomNotFoundException.class, () -> {
            inMemoryDatabase.getRooms().get(planning.searchFreeRoom(RoomKind.Meeting ,date_1, Duration.ofMinutes(150),150));
        });
        assertEquals(RoomNotFoundException.class, thrown.getClass());
    }

    @Test
    void searchFreeRoomTestWithKindAndBestSize() throws AlreadyExistingRoomException, RoomNotFoundException {
        manager.create("Salle4", RoomKind.Amphitheatre,10);
        Room find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(RoomKind.Amphitheatre ,date_1, Duration.ofMinutes(150),8));
        assertEquals("Salle4",find.getName());
        manager.create("Salle5", RoomKind.Meeting,150);
        find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(RoomKind.Meeting ,date_1, Duration.ofMinutes(150),150));
        assertEquals("Salle5",find.getName());
    }

    @Test
    void searchRoomTest() throws RoomNotFoundException {
        Room find = inMemoryDatabase.getRooms().get(planning.searchRoom("Salle1"));
        assertEquals(find.getCapacity(),25);
        find = inMemoryDatabase.getRooms().get(planning.searchRoom("Salle3"));
        assertEquals(find.getCapacity(),150);
    }

    @Test
    void searchRoomTestException(){
        RoomNotFoundException thrown = assertThrows(RoomNotFoundException.class, () -> {
            inMemoryDatabase.getRooms().get(planning.searchRoom("Salle4"));
        });
        assertEquals(RoomNotFoundException.class, thrown.getClass());
    }
}
