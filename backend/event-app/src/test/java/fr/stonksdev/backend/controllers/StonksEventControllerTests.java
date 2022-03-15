package fr.stonksdev.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.controllers.dto.StonksEventDTO;
import fr.stonksdev.backend.entities.StonksEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StonksEventController.class) // start only the specified MVC front controller and no other Spring components nor the server -> Unit test of the controller
@AutoConfigureWebClient // Added to avoid error on RestTemplateBuilder missing
public class StonksEventControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StonksEventManager mockedEventManager;

    @Test
    void registerEventTest() throws Exception {
        StonksEvent event = new StonksEvent("test",22, LocalDateTime.now(),LocalDateTime.now());
        StonksEventDTO eventDTO = new StonksEventDTO(event.getName(),event.getAmountOfPeople(),event.getStartDate(),event.getEndDate());
        when(mockedEventManager.createEvent(any(String.class),any(int.class),any(LocalDateTime.class),any(LocalDateTime.class)))
                .thenReturn(event);

        /*mockMvc.perform(post(StonksEventController.EVENTS_URI+"/register")
                .content(toJson(eventDTO)).contentType(APPLICATION_JSON).accept(APPLICATION_JSON));*/
                //.andExpect(jsonPath("$.name", contains(event.getName()))));
    }

    private String toJson(Object object){
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
