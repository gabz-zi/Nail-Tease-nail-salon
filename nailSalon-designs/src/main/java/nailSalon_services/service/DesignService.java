package nailSalon_services.service;


import jakarta.transaction.Transactional;
import nailSalon_services.model.dto.AddDesignDTO;
import nailSalon_services.model.dto.DesignDTO;
import nailSalon_services.model.entity.Design;
import nailSalon_services.model.entity.User;
import nailSalon_services.model.enums.Category;
import nailSalon_services.repository.DesignRepository;
import nailSalon_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DesignService {
    private final DesignRepository designRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public DesignService(DesignRepository designRepository, UserRepository userRepository) {
        this.designRepository = designRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void create(AddDesignDTO data, String username) {
        Optional<User> byName = userRepository.findByUsername(username);
        if (byName.isEmpty()) {
            throw new RuntimeException("User not found for username: " + username);
        }

        Design design = new Design();
        design.setName(data.getName());
        design.setMadeBy(byName.get());
        design.setCategory(Category.valueOf(data.getCategory().toUpperCase()));

        if (data.getImageUrl() == null || data.getImageUrl().isEmpty()) {
            throw new RuntimeException("Image file is missing"); // Ensure file is provided
        }

        // Save the uploaded image
        String imageUrl = saveFile(data.getImageUrl());
        design.setImageUrl(imageUrl);

        designRepository.save(design);
    }

    private String saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }

        return "/uploads/" + fileName;
    }

    public List<DesignDTO> findAll() {
        return designRepository.findAll().stream()
                .map(design -> new DesignDTO(
                        design.getId(),
                        design.getName(),
                        design.getCategory().name(),
                        design.getImageUrl(),
                        design.getMadeBy().getUsername() // Assuming getMadeBy() is not null
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public void delete(Long id) {
        Design design = designRepository.findById(id).get();
        User employee = design.getMadeBy();
        employee.getAddedDesigns().remove(design);
        userRepository.save(employee);
        designRepository.delete(design);
    }

}

