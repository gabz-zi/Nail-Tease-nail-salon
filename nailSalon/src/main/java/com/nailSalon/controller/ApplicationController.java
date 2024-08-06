package com.nailSalon.controller;

import com.nailSalon.model.dto.ApplicantDTO;
import com.nailSalon.model.dto.ApplicationFormDTO;
import com.nailSalon.model.entity.ExperienceLevel;
import com.nailSalon.service.ApplicantService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class ApplicationController {
    private final ApplicantService applicantService;

    public ApplicationController(ApplicantService applicantService) {
        this.applicantService = applicantService;
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


        boolean successful = applicantService.addApplicationFormToUserWithUsername(applicationFormDTO, userDetails.getUsername());

        if (!successful) {
            redirectAttributes.addFlashAttribute("applicationNotSubmitted", true);
        } else {
            redirectAttributes.addFlashAttribute("applicationSubmitted", true);
        }
        return "redirect:/apply-job";
    }

    @Transactional
    @GetMapping("/applicants")
    public ModelAndView applicants(){
        ModelAndView modelAndView = new ModelAndView("applicants");
        List<ApplicantDTO> applicantDTOS = applicantService.findAllApplicants();
        modelAndView.addObject("applicantsDTO", applicantDTOS);
        return modelAndView;
    }

    @PostMapping("/users/hire/{id}")
    public String hireUser(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        applicantService.hireUser(id);
        redirectAttributes.addFlashAttribute("hiredSuccessfully", true);
        return "redirect:/applicants";
    }

    @PostMapping("/appointments/reject/{id}")
    public String rejectUser(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        applicantService.rejectUser(id);
        redirectAttributes.addFlashAttribute("rejectedSuccessfully", true);
        return "redirect:/applicants";
    }
}
