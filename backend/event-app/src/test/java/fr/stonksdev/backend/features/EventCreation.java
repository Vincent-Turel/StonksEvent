package fr.stonksdev.backend.features;

import fr.stonksdev.backend.components.EventManager;
import fr.stonksdev.backend.components.InMemoryDatabase;
import fr.stonksdev.backend.entities.Duration;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CucumberContextConfiguration
@SpringBootTest
public class EventCreation {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");

    @Autowired
    private EventManager eventManager;

    @Autowired
    private InMemoryDatabase memory;

    private String eventId;

    @Before
    public void settingUpContext() {
        memory.flush();
    }

    @Etantdonné("un événement {string} avec maximum {int} personnes commencant le {string} et se terminant le {string}")
    public void createAnEvent(String eventName, int amountOfPeoples, String startDate, int duration) {
        LocalDateTime parsedStart = LocalDateTime.parse(startDate, formatter);
        Duration parsedDuration = Duration.ofMinutes(duration);

        eventId = eventManager.create(eventName, amountOfPeoples, parsedStart, parsedDuration).getId();
    }

    @Quand("les informations sont passées au controller")
    public void testToChange() {
        // Write code here that turns the phrase above into concrete actions
    }

    @Alors("l'événement {string} est créé")
    public void checkEventHasBeenCreated(String eventName) {
        //TODO
    }
}
