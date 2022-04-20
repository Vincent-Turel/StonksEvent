package fr.stonksdev.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.stonksdev.cli.CliContext;
import fr.stonksdev.cli.model.Activity;
import fr.stonksdev.cli.model.Duration;
import fr.stonksdev.cli.model.StonksEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ActivityCommand.class)
public class ActivityCommandTest {

    @MockBean
    CliContext cliContext;

    @Autowired
    private ActivityCommand client;

    @Autowired
    private MockRestServiceServer serverMock;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void createActivityTest() throws Exception {
        Map<String, StonksEvent> eventMap = new HashMap<>();
        LocalDateTime startDate = LocalDateTime.parse("01/01/2000 01:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("02/02/2002 22:22", formatter);
        StonksEvent event = new StonksEvent("Test", 1, startDate, endDate);
        long id = 1L;
        event.id = id;
        eventMap.put("Test", event);
        when(cliContext.getEvents()).thenReturn(eventMap);

        Activity activity = new Activity(startDate, Duration.ofMinutes(1), "activity", 10);

        String json = mapper.writeValueAsString(activity);

        serverMock.expect(requestTo("/events/" + id + "/activities"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        assertEquals(activity, client.createActivity("Test","activity", 1, "01/01/2000 01:00",10));

        assertThrows(DateTimeParseException.class, () -> client.createActivity("Test","activity", 1, "wrongdate",10));
    }

    @Test
    public void updateActivityTest() throws Exception {

        Map<String, StonksEvent> eventMap = new HashMap<>();
        LocalDateTime startDate = LocalDateTime.parse("01/01/2000 01:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("02/02/2002 22:22", formatter);
        StonksEvent event = new StonksEvent("Test", 1, startDate, endDate);
        long eventId = 1L;
        event.id = eventId;
        eventMap.put("Test", event);
        when(cliContext.getEvents()).thenReturn(eventMap);

        Map<String, Activity> activityMap = new HashMap<>();
        Activity activity = new Activity(startDate, Duration.ofMinutes(1), "activity", 10);
        long activityId = 1L;
        activity.id = activityId;
        activityMap.put("activity", activity);
        when(cliContext.getActivities()).thenReturn(activityMap);

        String json = mapper.writeValueAsString(activity);

        serverMock.expect(requestTo("/events/" + eventId + "/activities/" + activityId))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        assertEquals(activity, client.updateActivity("Test","activity", 1, "01/01/2000 01:00",10));

        assertThrows(DateTimeParseException.class, () -> client.updateActivity("Test", "activity", 1,"not parsable date",10));
    }

    @Test
    public void activitiesFromEventTest() throws Exception {
        Map<String, StonksEvent> eventMap = new HashMap<>();
        LocalDateTime startDate = LocalDateTime.parse("01/01/2000 01:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("02/02/2002 22:22", formatter);
        StonksEvent event = new StonksEvent("Test", 1, startDate, endDate);
        long id = 1L;
        event.id = id;
        eventMap.put("Test", event);
        when(cliContext.getEvents()).thenReturn(eventMap);

        serverMock
                .expect(requestTo("/events/" + id + "/activities"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("an activity", MediaType.APPLICATION_JSON));

        assertEquals("an activity", client.activitiesFromEvent("Test"));

    }

    @Test
    public void activitiesTest() {
        Map<String, Activity> map = new HashMap<>();
        when(cliContext.getActivities()).thenReturn(map);

        assertEquals(map.toString(), client.activities());
    }
}
