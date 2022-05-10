package fr.stonksdev.backend.entities.dto;

import java.util.List;

public class StonksEventWrapperDTO {
    public List<StonksEventDTO> list;

    public StonksEventWrapperDTO(List<StonksEventDTO> list) {
        this.list = list;
    }

    public StonksEventWrapperDTO(){

    }

}
