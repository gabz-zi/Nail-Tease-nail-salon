package com.nailSalon.controller;

import com.nailSalon.model.NailSalonUserDetails;
import com.nailSalon.model.dto.DesignHomeDTO;
import com.nailSalon.service.DesignService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {
    private final DesignService designService;

    public HomeController(DesignService designService) {
        this.designService = designService;
    }

    @GetMapping("/")
    public String nonLoggedIndex() {

        return "index";
    }

    @GetMapping("/home")
    @Transactional
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("home");
        DesignHomeDTO designsForHomePage = designService.getDesignsForHomePage();

        if (userDetails instanceof NailSalonUserDetails nailSalonUserDetails) {
            modelAndView.addObject("welcomeMessage", nailSalonUserDetails.getUsername());
        } else {
            modelAndView.addObject("welcomeMessage", "Anonymous");
        }

        modelAndView.addObject("designHomeDTO", designsForHomePage);

        return modelAndView;
    }
}
