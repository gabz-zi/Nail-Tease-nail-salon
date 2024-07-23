package com.nailSalon.service;

import com.nailSalon.model.dto.*;
import com.nailSalon.model.entity.Design;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.DesignRepository;
import com.nailSalon.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DesignService {
    private final DesignRepository designRepository;
    private final UserRepository userRepository;

    public DesignService(DesignRepository designRepository, UserRepository userRepository) {
        this.designRepository = designRepository;
        this.userRepository = userRepository;
    }

    public boolean create(AddDesignDTO data) {

        Optional<User> byId = userRepository.findById(1L); //LOGGED IN USER ID MUST BE
//TODO
        if (byId.isEmpty()) {
            return false;
        }


        Design design = new Design();
        design.setName(data.getName());
        design.setMadeBy(byId.get());
        design.setImageUrl(data.getImageUrl());
        design.setCategory(data.getCategory());

        designRepository.save(design);

        return true;
    }

    public DesignHomeDTO getDesignsForHomePage() {
        List<Design> designs = designRepository.findAll();

        List<MyDesignsDTO> myDesigns = new ArrayList<>();
        List<OtherDesignsDTO> otherDesigns = new ArrayList<>();
        List<FavsDesigns> favsDesigns = new ArrayList<>();

        for (Design design : designs) {
            String loggedUsername = "new";  //logged usr username
//TODO
            if (design.getMadeBy().getUsername().equals(loggedUsername)) {
                myDesigns.add(new MyDesignsDTO(design));
                User user = userRepository.findByUsername(loggedUsername).get();
                if (design.getMadeBy().getUsername().equals(loggedUsername) && user.getFavouritePaintings().contains(design)) {
                    favsDesigns.add(new FavsDesigns(design));
                }
                if (design.isInFavorites()) {
                    favsDesigns.add(new FavsDesigns(design));
                }
            } else {
                otherDesigns.add(new OtherDesignsDTO(design));
            }
        }

        return new DesignHomeDTO(myDesigns, otherDesigns, favsDesigns);
    }

    public void remove(Long id) {
        designRepository.deleteById(id);
    }

    @Transactional
    public void addToFavourites(Long id, long paintingId) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            return;
        }

        Optional<Design> paintingOpt = designRepository.findById(paintingId);

        if (paintingOpt.isEmpty()) {
            return;
        }

        userOpt.get().addFavourite(paintingOpt.get());

        userRepository.save(userOpt.get());
    }
}
