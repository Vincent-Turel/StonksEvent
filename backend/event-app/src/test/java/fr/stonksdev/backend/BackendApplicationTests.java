package fr.stonksdev.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private BackendApplication backend;

    @Test
    void itWorks() {
        assertEquals(2 + 2, 4);
    }

    /*
    @Test
    void contextLoads() {
    }*/
}
