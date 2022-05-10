package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Planning;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class PlanningDTO {
    public Long id;

    public StonksEventDTO event;

    public List<TimeSlotDTO> timeSlots;

    public PlanningDTO(StonksEvent event, List<TimeSlot> timeSlotList) {
        this.event = convertEventToDto(event);
        timeSlots = new ArrayList<>();
        for (TimeSlot slot: timeSlotList) {
            timeSlots.add(convertTimeSlotToDto(slot));
        }
    }

    public PlanningDTO() {
    }

    private StonksEventDTO convertEventToDto(StonksEvent event) {
        StonksEventDTO eventDTO = new StonksEventDTO(event.getName(), event.getAmountOfPeople(), event.getStartDate(), event.getEndDate());
        eventDTO.id = event.getId();
        return eventDTO;
    }

    private TimeSlotDTO convertTimeSlotToDto(TimeSlot slot){
        TimeSlotDTO slotDTO = new TimeSlotDTO(slot.getActivity(),slot.getBeginning(),slot.getDuration());
        slotDTO.id = slot.getId();
        return slotDTO;
    }
}
