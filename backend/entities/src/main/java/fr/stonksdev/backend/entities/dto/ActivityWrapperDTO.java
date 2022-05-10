package fr.stonksdev.backend.entities.dto;

import java.util.List;

public class ActivityWrapperDTO {
    public List<ActivityDTO> list;

    public ActivityWrapperDTO(List<ActivityDTO> list) {
        this.list = list;
    }

    public ActivityWrapperDTO(){

    }

}
