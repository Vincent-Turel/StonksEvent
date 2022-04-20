package fr.stonksdev.backend.features;

import fr.stonksdev.backend.components.ActivityManager;
import fr.stonksdev.backend.components.StonksEventManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@CucumberContextConfiguration
@SpringBootTest
public class EventCreation {

    @Autowired
    private StonksEventManager eventManager;
    @Autowired
    private ActivityManager activityManager;

    private StonksEvent event;

    @Autowired
    @MockBean
    private Mail mailMock;

    @Given("a name {string} for an event")
    public void setup(String test) {
        when(mailMock.send(anyString(), anyString(), anyString())).thenReturn(true);
    }

    @When("{string} create an event named {string} with {int} people between the {int} - {int} - {int} , {int} h {int}, and {int} - {int} - {int} , {int} h {int}")
    public void anOrganizerCreateAnEventNamedWithPeopleBetweenTheHAndH(String arg, String arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11) throws AlreadyExistingEventException {
        LocalDateTime date1 = LocalDateTime.of(arg4, arg3, arg2, arg5, arg6);
        LocalDateTime date2 = LocalDateTime.of(arg9, arg8, arg7, arg10, arg11);
        event = eventManager.createEvent(arg0, arg1, date1, date2);
    }

    @Then("there is {int} event")
    public void thereIsEvent(int arg0) {
        assertEquals(arg0, eventManager.getAllEvent().size());
    }

    @And("its name is {string}")
    public void itsNameIs(String arg0) {
        assertEquals(arg0, event.getName());
    }

    @And("the number of people is {int}")
    public void theNumberOfPeopleIs(int arg0) {
        assertEquals(arg0, event.getAmountOfPeople());
    }

    @And("the start date is {int} - {int} - {int} , {int} h {int}")
    public void theStartDateH(int arg0, int arg1, int arg2, int arg3, int arg4) {
        LocalDateTime date = LocalDateTime.of(arg2, arg1, arg0, arg3, arg4);
        assertEquals(date, event.getStartDate());
    }

    @And("the end date is {int} - {int} - {int} , {int} h {int}")
    public void theEndDateIsH(int arg0, int arg1, int arg2, int arg3, int arg4) {
        LocalDateTime date = LocalDateTime.of(arg2, arg1, arg0, arg3, arg4);
        assertEquals(date, event.getEndDate());
    }

    // HERE FAIRE AVEC JPA
    @Then("she change the amount of people to {int}")
    public void sheChangeTheAmountOfPeopleTo(int arg0) throws EventNotFoundException {
        /*StonksEvent event = eventManager.getAllEvent().get(id); WAIT JPA
        eventManager.updateEvent(id,
                arg0, event.getStartDate(),
                event.getEndDate());
        assertEquals(arg0, event.getAmountOfPeople());*/
    }

    @And("{string} create an activity named {string} with {int} people, the {int} - {int} - {int} , {int} h {int} , for {int} minutes")
    public void createAnActivityNamedWithPeopleTheHForMinutes(String arg0, String arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) throws AlreadyExistingActivityException {
        Activity activity = activityManager.createActivity(LocalDateTime.of(arg5, arg4, arg3, arg6, arg7), Duration.ofMinutes(arg8), arg1, arg2, event);
        assertEquals(arg1, activity.getName());
    }

    // HERE FAIRE AVEC JPA
    @Then("there is only {int} activity on the event named {string}")
    public void thereIsOnlyActivityOnTheEventNamed(int arg0, String arg1) {
        /*System.out.println(eventManager.findByName(arg1).get().getId());
        assertEquals(arg0, eventManager.getAllActivities().size());*/
    }
}
