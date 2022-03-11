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
        manager.create("Salle1", RoomKind.Amphitheatre,150);
        manager.create("Salle2", RoomKind.Meeting,25);
        manager.create("Salle3", RoomKind.Meeting,75);
    }

    @Test
    void searchFreeRoomTestWithoutKind() throws RoomNotFoundException {
        Room find = inMemoryDatabase.getRooms().get(planning.searchFreeRoom(date_1, Duration.ofMinutes(150),50));
        assertEquals("Salle1",find.getName());
    }
}
