package com.nailSalon.controller;

import com.nailSalon.model.dto.AddDesignDTO;
import com.nailSalon.model.dto.DesignHomeDTO;
import com.nailSalon.service.DesignService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DesignController {
    private final DesignService designService;

    public DesignController(DesignService designService) {
        this.designService = designService;
    }

    @ModelAttribute("designData")
    public AddDesignDTO designData() {
        return new AddDesignDTO();
    }

    @GetMapping("/add-design")
    public String addDesign() {

        return "add-design";
    }

    @GetMapping("/gallery")
    public String viewGallery(Model model) {

        model.addAttribute("designs", designService.findAll());

        return "gallery";
    }



    @PostMapping("/add-design")
    @Transactional
    public String doAddDesign(
            @Valid AddDesignDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("designData", data);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.designData", bindingResult);
            return "redirect:/add-design";
        }

        boolean success = designService.create(data, userDetails.getUsername());

        if (!success) {
            redirectAttributes.addFlashAttribute("designData", data);
            return "redirect:/add-design";
        }
        redirectAttributes.addFlashAttribute("addedSuccessfully", true);
        return "redirect:/add-design";
    }

    @GetMapping("/designs/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {

        designService.remove(id);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/designs/add-to-favourites/{recipeId}")
    public String addToFavourites(@PathVariable long recipeId, @AuthenticationPrincipal UserDetails userDetails) {

        designService.addToFavourites(userDetails.getUsername(), recipeId);
        return "redirect:/home";
    }

    @DeleteMapping("/designs/remove/{id}")
    public String removeDesign(@PathVariable Long id) {
        designService.delete(id);
        return "redirect:/gallery";
    }
}
