package com.nailSalon.controller;

import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.model.view.TodaysAppointmentView;
import com.nailSalon.service.TodaysAppointmentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TodaysAppointmentController {

    private final TodaysAppointmentService todaysAppointmentService;

    public TodaysAppointmentController(TodaysAppointmentService todaysAppointmentService) {
        this.todaysAppointmentService = todaysAppointmentService;
    }

    @Transactional
    @GetMapping("/todays-appointments")
    public ModelAndView forToday(@AuthenticationPrincipal UserDetails userDetails){
        ModelAndView modelAndView = new ModelAndView("todays-appointments");
        List<TodaysAppointmentView> todaysAppointmentViews = todaysAppointmentService.findAllAppointmentsForToday(userDetails.getUsername());
        modelAndView.addObject("forToday", todaysAppointmentViews);
        return modelAndView;
    }
}
