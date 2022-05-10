package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;

import javax.persistence.Id;
import java.time.LocalDateTime;

public class TimeSlotDTO {
    @Id
    public Long id;

    public ActivityDTO activity;
    public LocalDateTime beginning;
    public Duration duration;

    public TimeSlotDTO() {
    }

    public TimeSlotDTO(Activity activity, LocalDateTime beginning, Duration duration) {
        this.activity = convertActivityToDto(activity);
        this.beginning = beginning;
        this.duration = duration;
    }

    private ActivityDTO convertActivityToDto(Activity activity) {
        ActivityDTO activityDTO = new ActivityDTO(activity.getName(), activity.getMaxPeopleAmount(), activity.getBeginning(), activity.getDuration());
        activityDTO.id = activity.getId();
        return activityDTO;
    }

}
