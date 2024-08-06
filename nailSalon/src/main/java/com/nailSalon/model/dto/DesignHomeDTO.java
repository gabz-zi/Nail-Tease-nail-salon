package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Design;

import java.util.List;

public class DesignHomeDTO {
    private List<MyDesignsDTO> myDesigns;
    private List<Design> designs;
    private List<FavsDesigns> favoriteDesigns;

    public DesignHomeDTO(List<MyDesignsDTO> myDesigns, List<Design> designs
    , List<FavsDesigns> favoriteDesigns) {
        this.myDesigns = myDesigns;
        this.designs = designs;
        this.favoriteDesigns = favoriteDesigns;
    }

    public List<MyDesignsDTO> getMyDesigns() {
        return myDesigns;
    }

    public void setMyDesigns(List<MyDesignsDTO> myDesigns) {
        this.myDesigns = myDesigns;
    }

    public List<Design> getDesigns() {
        return designs;
    }

    public void setDesigns(List<Design> designs) {
        this.designs = designs;
    }

    public List<FavsDesigns> getFavoriteDesigns() {
        return favoriteDesigns;
    }

    public void setFavoriteDesigns(List<FavsDesigns> favoriteDesigns) {
        this.favoriteDesigns = favoriteDesigns;
    }
}
