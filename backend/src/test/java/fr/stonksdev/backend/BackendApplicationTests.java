package fr.stonksdev.backend;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import org.junit.jupiter.api.Test;
import junit.framework.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void simpleEventCreation() {
        /*
        StonksEvent e = new StonksEvent();

        e.addRoom(new Room());
        e.addRoom(new Room());

        e.addActivity(new Activity());
        e.addActivity(new Activity());

        assertThrows(RuntimeException.class, () -> e.addActivity(new Activity()));

         */
    }
}
