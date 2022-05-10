package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.entities.StonksEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface StonksEventModifier {
    /*
    CREATE/MODIFY AN EVENT :
     */
    StonksEvent createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws AlreadyExistingEventException;

    StonksEvent updateEvent(Long eventId, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws EventNotFoundException;

    void deleteEvent(StonksEvent event);

    List<StonksEvent> getAllEvent();
}
