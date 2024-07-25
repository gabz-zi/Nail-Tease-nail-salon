package com.nailSalon.controller;

import com.nailSalon.model.dto.AddNailServiceDTO;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.service.NailServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
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

        nailServiceService.create(data);

        return "redirect:/home";
    }

    @GetMapping("/services")
        public String getServices(Model model) {
        Map<Category, List<NailService>> allServices = nailServiceService.findAllByCategory();
        List<NailService> gels = allServices.get(Category.valueOf("GEL"));
        String minPriceForGel = nailServiceService.getMinPriceForCertainCategory(gels);
        model.addAttribute("gelsData", gels);
        model.addAttribute("minPriceGel", minPriceForGel);
            return "services";
    }



}
