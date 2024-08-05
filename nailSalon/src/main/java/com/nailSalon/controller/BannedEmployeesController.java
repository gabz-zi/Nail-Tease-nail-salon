package com.nailSalon.controller;

import com.nailSalon.model.view.BannedEmployeeView;
import com.nailSalon.service.BannedEmployeeService;
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
public class BannedEmployeesController {

    private final BannedEmployeeService bannedEmployeeService;

    public BannedEmployeesController(BannedEmployeeService bannedEmployeeService) {
        this.bannedEmployeeService = bannedEmployeeService;
    }

    @Transactional
    @GetMapping("/banned-employees")
    public ModelAndView forToday(@AuthenticationPrincipal UserDetails userDetails){
        ModelAndView modelAndView = new ModelAndView("banned-employees");
        List<BannedEmployeeView> bannedEmployeeViews = bannedEmployeeService.findAllBannedEmployees();
        modelAndView.addObject("bannedEmployees", bannedEmployeeViews);
        return modelAndView;
    }

    @PostMapping("/users/fire/{id}")
    public String fireEmployee(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        bannedEmployeeService.fireEmployee(id);
        redirectAttributes.addFlashAttribute("firedSuccessfully", true);
        return "redirect:/banned-employees";
    }
}
