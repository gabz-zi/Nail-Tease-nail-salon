package com.nailSalon.controller;

import com.nailSalon.model.dto.AddAppointmentDTO;
import com.nailSalon.model.dto.AddDesignDTO;
import com.nailSalon.model.dto.ApplicationFormDTO;
import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.ExperienceLevel;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.CVRepository;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.CVService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
public class ApplicationController {
    private final CVService cvService;

    public ApplicationController(CVService cvService) {
        this.cvService = cvService;
    }

    @ModelAttribute("applicationFormDTO")
    public ApplicationFormDTO applicationData() {
        return new ApplicationFormDTO();
    }
    @GetMapping("/apply-job")
    public String showApplicationForm(Model model) {
        model.addAttribute("experienceLevels", Arrays.asList(ExperienceLevel.values()));
        return "apply-job";
    }

    @PostMapping("/apply-job")
    public String applyForJob(@Valid ApplicationFormDTO applicationFormDTO, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("applicationFormDTO", applicationFormDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.applicationFormDTO", bindingResult);
            return "redirect:/apply-job";
        }


        cvService.addApplicationFormToUserWithUsername(applicationFormDTO, userDetails.getUsername());

        redirectAttributes.addFlashAttribute("applicationSubmitted", true);
        return "redirect:/apply-job";
    }
}
