package com.nailSalon.controller;

import com.nailSalon.model.dto.AddAppointmentDTO;
import com.nailSalon.service.AppointmentService;
import com.nailSalon.service.NailServiceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final NailServiceService nailServiceService;

    public AppointmentController(AppointmentService appointmentService, NailServiceService nailServiceService) {
        this.appointmentService = appointmentService;
        this.nailServiceService = nailServiceService;
    }

    @ModelAttribute("appointmentAddDTO")
    public AddAppointmentDTO getAppointmentAddDTO() {
        return new AddAppointmentDTO();
    }

    @GetMapping("/make-appointment1")
    public String makeAppointment(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("services", nailServiceService.getAllServicesForAppointmentPage());
        return "make-appointment1";
    }


    @PostMapping("/make-appointment1")
    public String postMakeAppointment(@Valid AddAppointmentDTO appointmentAddDTO, BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      @AuthenticationPrincipal UserDetails userDetails,  HttpServletRequest request) {

        // Log raw data
        String rawDateTime = request.getParameter("madeFor");
        System.out.println("Raw madeFor value: " + rawDateTime);
        String rawService = request.getParameter("service");
        System.out.println("Raw service value: " + rawService);

        // Manually parse the datetime string
        String madeForStr = request.getParameter("madeFor");
        System.out.println("Raw madeFor value: " + madeForStr);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime parsedDate = LocalDateTime.parse(madeForStr, formatter);
            System.out.println("Parsed Date: " + parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            LocalDateTime parsedDate = LocalDateTime.parse("2024-07-31T21:20", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("Parsed Date: " + parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            redirectAttributes.addFlashAttribute("appointmentAddDTO", appointmentAddDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.appointmentAddDTO", bindingResult);
            return "redirect:/appointments/make-appointment1";
        }
        redirectAttributes.addFlashAttribute("addedSuccessfully", true);
        appointmentService.addAppointmentToUserWithUsername(appointmentAddDTO, userDetails.getUsername());
        return "redirect:/appointments/make-appointment1";
    }
}
