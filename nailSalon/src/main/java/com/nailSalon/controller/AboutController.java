package com.nailSalon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about-us")
    public String aboutUs() {
       /* // Replace with your actual location coordinates
        String latitude = "34.0635384";
        String longitude = "-118.3992791";

        // Generate Google Maps static map URL
        String googleMapImageUrl = "https://maps.googleapis.com/maps/api/staticmap"
                + "?center=" + latitude + "," + longitude
                + "&zoom=10" // Adjust zoom level as needed
                + "&size=600x400" // Adjust size of the map image
                + "&markers=color:red%7Clabel:A%7C" + latitude + "," + longitude
                + "&key=AIzaSyBHeJP4AqMKfHIyGH8I3fi_WfrltcVHonU"; // Replace with your actual Google Maps API key

        // Add attributes to the model
        ModelAndView modelAndView = new ModelAndView("about-us");
        modelAndView.addObject("googleMapImageUrl", googleMapImageUrl);*/

        return "about-us"; // Renders about.html Thymeleaf template
    }
}
