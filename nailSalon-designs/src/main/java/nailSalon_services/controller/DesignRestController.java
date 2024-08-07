package nailSalon_services.controller;


import nailSalon_services.model.dto.AddDesignDTO;
import nailSalon_services.model.dto.DesignDTO;
import nailSalon_services.model.entity.Design;
import nailSalon_services.model.enums.Category;
import nailSalon_services.service.DesignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/designs")
public class DesignRestController {

    private final DesignService designService;

    public DesignRestController(DesignService designService) {
        this.designService = designService;
    }

    @GetMapping
    public List<DesignDTO> getAllDesigns() {
        return designService.findAll();
    }


    @PostMapping
    public ResponseEntity<Void> createDesign(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image,
            @RequestParam("madeBy") String username
    ) {
        AddDesignDTO designDTO = new AddDesignDTO();
        designDTO.setName(name);
        designDTO.setCategory(category);
        designDTO.setImageUrl(image);

        designService.create(designDTO, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDesign(@PathVariable Long id) {
        designService.delete(id);
        return ResponseEntity.ok().build();
    }

}

