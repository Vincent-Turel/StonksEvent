package fr.stonksdev.backend.features;

import fr.stonksdev.backend.components.EventManager;
import fr.stonksdev.backend.components.InMemoryDatabase;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@CucumberContextConfiguration
@SpringBootTest
public class EventCreation {

    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private EventManager eventManager;

    @Autowired
    private InMemoryDatabase memory;

    private String eventId;

    @Before
    public void settingUpContext() {
        memory.flush();
    }

    @Etantdonné("un événement {string] avec maximum {int} personnes commencant le {string} et se terminant le {string}")
    public void createAnEvent(String eventName, int amountOfPeoples, String startDate, String endDate) throws ParseException {
        eventId = eventManager.create(eventName, amountOfPeoples, formatter.parse(startDate), formatter.parse(endDate)).getId();
    }
    @Quand("les informations sont passées au controller")
    public void testToChange() {
        // Write code here that turns the phrase above into concrete actions
    }

    @Alors("l'événement {string} est créé")
    public void checkEventHasBeenCreated(String eventName){
        //TODO
    }


}
