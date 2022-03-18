package fr.stonksdev.cli.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.stonksdev.cli.CliContext;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(EventCommand.class)
public class EventCommandTest {

    @MockBean
    CliContext cliContext;

    @Autowired
    private EventCommand client;

    @Autowired
    private MockRestServiceServer serverMock;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();


    @Test
    public void createEventTest() throws JsonProcessingException {
        LocalDateTime startDate = LocalDateTime.parse("01/01/2000 01:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("02/02/2002 22:22", formatter);
        var event = new StonksEvent("Test", 1, startDate, endDate);

        String json = mapper.writeValueAsString(event);

        serverMock.expect(requestTo("/events"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        assertEquals(event, client.createEvent("Test",1, "01/01/2000 01:00","02/02/2002 22:22"));

        assertThrows(DateTimeParseException.class, () -> client.createEvent("Test", 1,"not parsable date","02/02/2002 22:22"));
    }

    @Test
    public void updateEventTest() throws Exception {

        Map<String, StonksEvent> eventMap = new HashMap<>();
        LocalDateTime startDate = LocalDateTime.parse("01/01/2000 01:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("02/02/2002 22:22", formatter);
        var event = new StonksEvent("Test", 1, startDate, endDate);
        var eventId = UUID.randomUUID();
        event.id = eventId;
        eventMap.put("Test", event);
        when(cliContext.getEvents()).thenReturn(eventMap);

        String json = mapper.writeValueAsString(event);

        serverMock.expect(requestTo("/events/" + eventId))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        assertEquals(event, client.updateEvent("Test",1, "01/01/2000 01:00","02/02/2002 22:22"));

        assertThrows(DateTimeParseException.class, () -> client.updateEvent("Test", 1,"not parsable date","02/02/2002 22:22"));
    }

    @Test
    public void listEventsTest() {
        Map<String, StonksEvent>  map = new HashMap<>();
        when(cliContext.getEvents()).thenReturn(map);

        assertEquals(map.toString(), client.events());
    }

}
