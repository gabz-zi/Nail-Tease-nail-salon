package com.nailSalon.controller;

import com.nailSalon.model.dto.AddDesignDTO;
import com.nailSalon.service.DesignService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DesignController {
    private final DesignService designService;

    public DesignController(DesignService designService, DesignService designService1) {
        this.designService = designService1;
    }

    @ModelAttribute("designData")
    public AddDesignDTO paintingData() {
        return new AddDesignDTO();
    }

    @GetMapping("/add-design")
    public String addDesign() {

        return "add-design";
    }

    @PostMapping("/add-design")
    @Transactional
    public String doAddDesign(
            @Valid AddDesignDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("designData", data);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.designData", bindingResult);

            return "redirect:/add-design";
        }

        boolean success = designService.create(data);

        if (!success) {
            redirectAttributes.addFlashAttribute("designData", data);

            return "redirect:/add-design";
        }
        redirectAttributes.addFlashAttribute("addedSuccessfully", true);
        return "redirect:/add-design";
    }

    @GetMapping("/paintings/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {

        designService.remove(id);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/paintings/add-to-favourites/{recipeId}")
    public String addToFavourites(@PathVariable long recipeId) {

        designService.addToFavourites(1L, recipeId);   // the id should change according to do logged user
 //TODO
        return "redirect:/home";
    }
}
