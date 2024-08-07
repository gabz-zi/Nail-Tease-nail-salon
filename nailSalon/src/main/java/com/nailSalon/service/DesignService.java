package com.nailSalon.service;

import com.nailSalon.model.dto.*;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.Design;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.DesignRepository;
import com.nailSalon.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DesignService {
    private final DesignRepository designRepository;
    private final UserRepository userRepository;
    private final DesignServiceClient designServiceClient;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public DesignService(DesignRepository designRepository, UserRepository userRepository, DesignServiceClient designServiceClient) {
        this.designRepository = designRepository;
        this.userRepository = userRepository;
        this.designServiceClient = designServiceClient;
    }




    @Transactional
    public boolean create(AddDesignDTO data, String username) {
        // Directly pass MultipartFile to the DesignServiceClient
        designServiceClient.createDesign(
                data.getName(),
                data.getCategory(),
                data.getImageUrl(), // Assuming this is MultipartFile
                username
        );

        return true;
    }
    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
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

   /* public boolean create(AddDesignDTO data, String username) {

        Optional<User> byName = userRepository.findByUsername(username);
        if (byName.isEmpty()) {
            return false;
        }


        Design design = new Design();
        design.setName(data.getName());
        design.setMadeBy(byName.get());
        design.setImageUrl(data.getImageUrl());
        design.setCategory(data.getCategory());

        designRepository.save(design);

        return true;
    } */

    public DesignHomeDTO getDesignsForHomePage(String username) {
        List<Design> designs = designRepository.findAll();

        List<MyDesignsDTO> myDesigns = new ArrayList<>();
        List<Design> otherDesigns = new ArrayList<>();
        List<FavsDesigns> favsDesigns = new ArrayList<>();

        for (Design design : designs) {
            if (design.getMadeBy().getUsername().equals(username)) {
                myDesigns.add(new MyDesignsDTO(design));
                User user = userRepository.findByUsername(username).get();
                if (design.getMadeBy().getUsername().equals(username) && user.getFavouriteDesigns().contains(design)) {
                    favsDesigns.add(new FavsDesigns(design));
                }
                if (design.isInFavorites()) {
                    favsDesigns.add(new FavsDesigns(design));
                }
            }
        }

        return new DesignHomeDTO(myDesigns, otherDesigns, favsDesigns);
    }



    public void remove(Long id) {
        designRepository.deleteById(id);
    }

    @Transactional
    public void addToFavourites(String username, long designId) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return;
        }

        Optional<Design> designOptional = designRepository.findById(designId);

        if (designOptional.isEmpty()) {
            return;
        }

        userOpt.get().addFavourite(designOptional.get());

        userRepository.save(userOpt.get());
    }


    public List<DesignDTO> findAll() {
        return designServiceClient.getAllDesigns().stream()
                .map(dto -> {
                    DesignDTO design = new DesignDTO();
                    design.setId(dto.getId());
                    design.setName(dto.getName());
                    design.setCategory(dto.getCategory());
                    design.setImageUrl(dto.getImageUrl());
                    design.setMadeByUsername(dto.getMadeByUsername());
                    return design;
                }).toList();
    }

    @Transactional
    public void delete(Long id) {
        designServiceClient.deleteDesign(id);
    }
}
