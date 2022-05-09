package fr.stonksdev.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Planning {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    private StonksEvent event;

    @OneToMany(mappedBy = "planning", cascade = CascadeType.PERSIST)
    private List<TimeSlot> timeSlots;

    public Planning(StonksEvent event) {
        this.event = event;
        timeSlots = new ArrayList<>();
    }

    public void addSlot(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
    }

    public void removeSlot(TimeSlot timeSlot) {
        timeSlots.remove(timeSlot);
    }

    public Planning() {

    }

    public int getSize() {
        return timeSlots.size();
    }

    public void clearSlots() {
        timeSlots.clear();
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public Planning getPlanningForRoom(Room room) {
        Planning planning = new Planning(event);
        timeSlots.stream().filter(slot -> slot.getActivity() != null).filter(slot -> slot.getActivity().getRoom() == room).forEach(planning::addSlot);
        timeSlots.sort(Comparator.comparing(TimeSlot::getBeginning));
        return planning;
    }
}
