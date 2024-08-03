package com.nailSalon.model.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AddAppointmentDTO {
    @Future(message = "Please select a future date and time!")
    @NotNull(message = "Please select date and time!")
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
