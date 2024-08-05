package com.nailSalon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CareersController {

    @GetMapping("/careers")
    public String careers() {
        return "/careers";
    }
}
