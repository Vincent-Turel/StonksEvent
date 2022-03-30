package fr.stonksdev.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.stonksdev.backend.components.InMemoryDatabase;
import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.dto.ActivtyDTO;
import fr.stonksdev.backend.entities.dto.StonksEventDTO;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StonksEventController.class)
// start only the specified MVC front controller and no other Spring components nor the server -> Unit test of the controller
@AutoConfigureWebClient // Added to avoid error on RestTemplateBuilder missing
public class StonksEventControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private InMemoryDatabase inMemoryDatabase;

    @MockBean
    private StonksEventManager mockedEventManager;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static String jsonConverter(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerEventTest() throws Exception {
        StonksEvent event = new StonksEvent("test", 22, LocalDateTime.parse("13/10/2022 08:00", formatter), LocalDateTime.parse("16/10/2022 16:00", formatter));
        StonksEventDTO eventDTO = new StonksEventDTO(event.getName(), event.getAmountOfPeople(), event.getStartDate(), event.getEndDate());
        when(mockedEventManager.createEvent(Mockito.any(String.class), Mockito.any(int.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(event);

        String jsonEvent = jsonConverter(eventDTO);
        mockMvc.perform(post(StonksEventController.EVENTS_URI)
                        .content(jsonEvent).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").value(event.getName()))
                .andExpect(jsonPath("$.amountOfPeople").value(event.getAmountOfPeople()));

    }

    @Test
    void updateEventTest() throws Exception {
        StonksEvent event = new StonksEvent("test", 22, LocalDateTime.parse("13/10/2022 08:00", formatter), LocalDateTime.parse("16/10/2022 16:00", formatter));
        StonksEvent eventUpdated = new StonksEvent("test", 55, LocalDateTime.parse("13/10/2022 08:00", formatter), LocalDateTime.parse("16/10/2022 16:00", formatter));
        StonksEventDTO eventDTO = new StonksEventDTO(event.getName(), event.getAmountOfPeople(), event.getStartDate(), event.getEndDate());
        when(mockedEventManager.updateEvent(eq(event.getId()), Mockito.any(int.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(eventUpdated);

        String jsonEvent = jsonConverter(eventDTO);
        mockMvc.perform(post(StonksEventController.EVENTS_URI + "/" + event.getId())
                        .content(jsonEvent).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").value(eventUpdated.getName()))
                .andExpect(jsonPath("$.amountOfPeople").value(eventUpdated.getAmountOfPeople()));

        when(mockedEventManager.updateEvent(Mockito.any(), Mockito.any(int.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenThrow(EventIdNotFoundException.class);

        mockMvc.perform(post(StonksEventController.EVENTS_URI + "/" + event.getId())
                        .content(jsonEvent).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void registerActivityTest() throws Exception {
        Activity activity = new Activity(LocalDateTime.parse("13/10/2022 08:00", formatter), Duration.ofMinutes(50), "test", 10, UUID.randomUUID());
        ActivtyDTO activtyDTO = new ActivtyDTO(activity.getName(), activity.getMaxPeopleAmount(), "", activity.getBeginning(), activity.getDuration());
        when(mockedEventManager.createActivity(Mockito.any(LocalDateTime.class), Mockito.any(Duration.class), Mockito.any(String.class), Mockito.any(UUID.class)))
                .thenReturn(activity);

        String jsonActivity = jsonConverter(activtyDTO);
        mockMvc.perform(post(StonksEventController.EVENTS_URI + "/" + UUID.randomUUID() + "/activities")
                        .content(jsonActivity).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").value(activity.getName()))
                .andExpect(jsonPath("$.maxPeopleAmount").value(activity.getMaxPeopleAmount()));

    }

    @Test
    void updateActivityTest() throws Exception {
        UUID eventId = UUID.randomUUID();

        Activity activity = new Activity(LocalDateTime.parse("13/10/2022 08:00", formatter), Duration.ofMinutes(50), "test", 10, eventId);
        Activity activityUpdated = new Activity(LocalDateTime.parse("13/10/2022 08:00", formatter), Duration.ofMinutes(90), "test", 15, eventId);

        ActivtyDTO activtyDTO = new ActivtyDTO(activity.getName(), activity.getMaxPeopleAmount(), "", activity.getBeginning(), activity.getDuration());
        when(mockedEventManager.updateActivity(Mockito.any(Activity.class), Mockito.any(int.class), Mockito.any(LocalDateTime.class), Mockito.any(Duration.class)))
                .thenReturn(activityUpdated);

        when(inMemoryDatabase.getActivities()).thenReturn(Map.of(activity.getActivityID(), activity));

        String jsonActivity = jsonConverter(activtyDTO);
        mockMvc.perform(post(StonksEventController.EVENTS_URI + "/" + eventId + "/activities/" + activity.getActivityID())
                        .content(jsonActivity).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").value(activityUpdated.getName()))
                .andExpect(jsonPath("$.maxPeopleAmount").value(activityUpdated.getMaxPeopleAmount()))
                .andExpect(jsonPath("$.duration.minutes").value(activityUpdated.getDuration().asMinutes()));
    }
}
