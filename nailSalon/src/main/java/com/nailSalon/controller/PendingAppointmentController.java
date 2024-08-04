package com.nailSalon.controller;

import com.nailSalon.model.view.MyAppointmentView;
import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.service.AppointmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PendingAppointmentController {
    private final AppointmentService appointmentService;

    public PendingAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Transactional
    @GetMapping("/pending-appointments")
    public ModelAndView pending(){
        ModelAndView modelAndView = new ModelAndView("pending-appointments");
        List<PendingAppointmentView> pendingAppointments = appointmentService.findAllPendingAppointments();
        modelAndView.addObject("pending", pendingAppointments);
        return modelAndView;
    }

}
