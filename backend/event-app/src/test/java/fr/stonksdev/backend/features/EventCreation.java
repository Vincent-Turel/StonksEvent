package fr.stonksdev.backend.features;

import fr.stonksdev.backend.components.InMemoryDatabase;
import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.interfaces.Mail;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest
public class EventCreation {

    @Autowired
    InMemoryDatabase inMemoryDatabase;
    @Autowired
    StonksEventManager eventManager;

    @Autowired
    @MockBean
    private Mail mailMock;

    @Given("a name {string} for an event")
    public void setup(String test) {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);
        eventManager.reset();
    }

    @When("{string} create an event named {string} with {int} people between the {int} - {int} - {int} , {int} h {int}, and {int} - {int} - {int} , {int} h {int}")
    public void anOrganizerCreateAnEventNamedWithPeopleBetweenTheHAndH(String arg, String arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11) throws AlreadyExistingEventException {
        LocalDateTime date1 = LocalDateTime.of(arg4, arg3, arg2, arg5, arg6);
        LocalDateTime date2 = LocalDateTime.of(arg9, arg8, arg7, arg10, arg11);
        eventManager.createEvent(arg0, arg1, date1, date2);
    }

    @Then("there is {int} event")
    public void thereIsEvent(int arg0) {
        assertEquals(arg0, eventManager.getAllEvent().size());
    }

    @And("its name is {string}")
    public void itsNameIs(String arg0) {
        assertEquals(arg0, eventManager.getAllEvent().get(eventManager.getEventIdList().get(0)).getName());
    }

    @And("the number of people is {int}")
    public void theNumberOfPeopleIs(int arg0) {
        assertEquals(arg0, eventManager.getAllEvent().get(eventManager.getEventIdList().get(0)).getAmountOfPeople());
    }

    @And("the start date is {int} - {int} - {int} , {int} h {int}")
    public void theStartDateH(int arg0, int arg1, int arg2, int arg3, int arg4) {
        LocalDateTime date = LocalDateTime.of(arg2, arg1, arg0, arg3, arg4);
        assertEquals(date, eventManager.getAllEvent().get(eventManager.getEventIdList().get(0)).getStartDate());
    }

    @And("the end date is {int} - {int} - {int} , {int} h {int}")
    public void theEndDateIsH(int arg0, int arg1, int arg2, int arg3, int arg4) {
        LocalDateTime date = LocalDateTime.of(arg2, arg1, arg0, arg3, arg4);
        assertEquals(date, eventManager.getAllEvent().get(eventManager.getEventIdList().get(0)).getEndDate());
    }

    @Then("she change the amount of people to {int}")
    public void sheChangeTheAmountOfPeopleTo(int arg0) throws EventIdNotFoundException {
        UUID id = eventManager.getEventIdList().get(0);
        StonksEvent event = eventManager.getAllEvent().get(id);
        eventManager.updateEvent(id,
                arg0, event.getStartDate(),
                event.getEndDate());
        assertEquals(arg0, event.getAmountOfPeople());
    }


    @And("{string} create an activity named {string} with {int} people, the {int} - {int} - {int} , {int} h {int} , for {int} minutes")
    public void createAnActivityNamedWithPeopleTheHForMinutes(String arg0, String arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
        eventManager.createActivity(LocalDateTime.of(arg5, arg4, arg3, arg6, arg7), Duration.ofMinutes(arg8), arg1, arg2, eventManager.getEventIdList().get(0));
        assertEquals(arg1, eventManager.getAnActivity(eventManager.getActivitiesId().get(0)).getName());
    }

    @Then("there is only {int} activity on the event named {string}")
    public void thereIsOnlyActivityOnTheEventNamed(int arg0, String arg1) throws EventIdNotFoundException {
        System.out.println(eventManager.findByName(arg1).getId());
        assertEquals(arg0, eventManager.getAllActivities().size());
    }
}
