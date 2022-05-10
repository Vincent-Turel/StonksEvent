package fr.stonksdev.cli.model;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Planning {
    public Long id;

    public StonksEvent event;

    public List<TimeSlot> timeSlots;

    public Planning(StonksEvent event) {
        this.event = event;
        timeSlots = new ArrayList<>();
    }

    public Planning() {
    }

    public String toString() {
        StringBuilder res = new StringBuilder("Planning ");
        if(event == null){
            res.append("with a NULL event, ");
        }
        else {
            res.append("with event :").append(event).append(",");
        }
        if(timeSlots == null){
            res.append("with a NULL timeSlots.");
        }
        else{
            int size = timeSlots.size();
            for (int i = 0; i < size; i++) {
                res.append(i + 1).append(". ").append(timeSlots.get(i).toString());
                if (i != size - 1) {
                    res.append(", ");
                }
            }
            res.append("}");
        }

        return res.toString();
    }
}
