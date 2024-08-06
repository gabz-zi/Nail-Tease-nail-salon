package com.nailSalon.service;

import com.nailSalon.model.dto.*;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    public DesignService(DesignRepository designRepository, UserRepository userRepository) {
        this.designRepository = designRepository;
        this.userRepository = userRepository;
    }



    @Transactional
    public boolean create(AddDesignDTO data, String username) {
        Optional<User> byName = userRepository.findByUsername(username);
        if (byName.isEmpty()) {
            return false;
        }

        Design design = new Design();
        design.setName(data.getName());
        design.setMadeBy(byName.get());
        design.setCategory(data.getCategory());

        // Save the uploaded image
        String imageUrl = saveFile(data.getImageUrl());
        design.setImageUrl(imageUrl);

        designRepository.save(design);

        return true;
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

    public List<Design> findAll() {
       return designRepository.findAll();
    }
}
