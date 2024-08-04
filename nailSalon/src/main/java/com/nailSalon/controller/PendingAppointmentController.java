package com.nailSalon.controller;

import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.service.PendingAppointmentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PendingAppointmentController {
    private final PendingAppointmentService pendingAppointmentService;

    public PendingAppointmentController(PendingAppointmentService pendingAppointmentService) {
        this.pendingAppointmentService = pendingAppointmentService;
    }

    @Transactional
    @GetMapping("/pending-appointments")
    public ModelAndView pending(@AuthenticationPrincipal UserDetails userDetails){
        ModelAndView modelAndView = new ModelAndView("pending-appointments");
        List<PendingAppointmentView> pendingAppointments = pendingAppointmentService.findAllPendingAppointments(userDetails.getUsername());
        modelAndView.addObject("pending", pendingAppointments);
        return modelAndView;
    }


    @PostMapping("/appointments/accept/{id}")
    public String acceptAppointment(RedirectAttributes redirectAttributes, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        pendingAppointmentService.acceptAppointment(id, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("acceptedSuccessfully", true);
        return "redirect:/pending-appointments";
    }

    @PostMapping("/appointments/decline/{id}")
    public String declineAppointment(RedirectAttributes redirectAttributes, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        pendingAppointmentService.declineAppointment(id, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("declinedSuccessfully", true);
        return "redirect:/pending-appointments";
    }


}
