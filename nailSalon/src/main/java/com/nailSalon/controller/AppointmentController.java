package com.nailSalon.controller;

import com.nailSalon.model.dto.AddAppointmentDTO;
import com.nailSalon.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @ModelAttribute("appointmentAddDTO")
    public AddAppointmentDTO getAppointmentAddDTO() {
        return new AddAppointmentDTO();
    }
}
