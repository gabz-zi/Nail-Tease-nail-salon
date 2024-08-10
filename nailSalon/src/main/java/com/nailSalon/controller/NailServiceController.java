package com.nailSalon.controller;

import com.nailSalon.model.dto.AddNailServiceDTO;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.service.NailServiceService;
import com.nailSalon.service.exception.RemoveServiceWithPresentAppointments;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class NailServiceController {

    private final NailServiceService nailServiceService;

    public NailServiceController(NailServiceService nailServiceService) {
        this.nailServiceService = nailServiceService;
    }

    @ModelAttribute("nailServiceData")
    public AddNailServiceDTO nailServiceData() {
        return new AddNailServiceDTO();
    }

    @GetMapping("/add-service")
    public String addNailService() {

        return "add-service";
    }



    @PostMapping("/add-service")
    @Transactional
    public String doAddService(
            @Valid AddNailServiceDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("nailServiceData", data);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.nailServiceData", bindingResult);

            return "redirect:/add-service";
        }
        redirectAttributes.addFlashAttribute("addedSuccessfully", true);
        nailServiceService.create(data);

        return "redirect:/add-service";
    }

    @GetMapping("/services")
        public String getServices(Model model) {
        Map<Category, List<NailService>> allServices = nailServiceService.findAllByCategory();
        List<NailService> gels = allServices.get(Category.valueOf("GEL"));
        model.addAttribute("gelsData", gels);
        if (!gels.isEmpty()) {
            String minPriceForGel = nailServiceService.getMinPriceForCertainCategory(gels);
            model.addAttribute("minPriceGel", minPriceForGel);
        }

        List<NailService> gelishes = allServices.get(Category.valueOf("GELISH"));
        model.addAttribute("gelishesData", gelishes);
        if (!gelishes.isEmpty()) {
            String minPriceForGelish = nailServiceService.getMinPriceForCertainCategory(gelishes);
            model.addAttribute("minPriceGelish", minPriceForGelish);
        }

        List<NailService> pedicures = allServices.get(Category.valueOf("PEDICURE"));
        model.addAttribute("pedicuresData", pedicures);
        if (!pedicures.isEmpty()) {
            String minPriceForPedicure = nailServiceService.getMinPriceForCertainCategory(pedicures);
            model.addAttribute("minPricePedicure", minPriceForPedicure);
        }

        List<NailService> therapies = allServices.get(Category.valueOf("THERAPY"));
        model.addAttribute("therapiesData", therapies);
        if (!therapies.isEmpty()) {
            String minPriceForTherapy = nailServiceService.getMinPriceForCertainCategory(therapies);
            model.addAttribute("minPriceTherapy", minPriceForTherapy);
        }
            return "services";
    }

    @DeleteMapping("/services/remove/{id}")
    public String removeService(@PathVariable Long id) {
        nailServiceService.delete(id);
        return "redirect:/services";
    }

    @ExceptionHandler(RemoveServiceWithPresentAppointments.class)
    public ModelAndView handleRemoveServiceHasAppointments(RemoveServiceWithPresentAppointments e) {
        ModelAndView modelAndView = new ModelAndView("remove-service-having-appointments");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }


}
