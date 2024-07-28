package com.nailSalon.model.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AddAppointmentDTO {
    @Future
    @NotNull
    private LocalDateTime madeFor;

    @NotNull
    private String service;

    public AddAppointmentDTO() {
    }

    public LocalDateTime getMadeFor() {
        return madeFor;
    }

    public void setMadeFor(LocalDateTime madeFor) {
        this.madeFor = madeFor;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
