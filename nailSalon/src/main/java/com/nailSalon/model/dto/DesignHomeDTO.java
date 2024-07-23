package com.nailSalon.model.dto;

import java.util.List;

public class DesignHomeDTO {
    private List<MyDesignsDTO> myDesigns;
    private List<OtherDesignsDTO> allOtherDesigns;
    private List<FavsDesigns> favoriteDesigns;

    public DesignHomeDTO(List<MyDesignsDTO> myDesigns, List<OtherDesignsDTO> allOtherDesigns
    , List<FavsDesigns> favoriteDesigns) {
        this.myDesigns = myDesigns;
        this.allOtherDesigns = allOtherDesigns;
        this.favoriteDesigns = favoriteDesigns;
    }

    public List<MyDesignsDTO> getMyDesigns() {
        return myDesigns;
    }

    public void setMyDesigns(List<MyDesignsDTO> myDesigns) {
        this.myDesigns = myDesigns;
    }

    public List<OtherDesignsDTO> getAllOtherDesigns() {
        return allOtherDesigns;
    }

    public void setAllOtherDesigns(List<OtherDesignsDTO> allOtherDesigns) {
        this.allOtherDesigns = allOtherDesigns;
    }

    public List<FavsDesigns> getFavoriteDesigns() {
        return favoriteDesigns;
    }

    public void setFavoriteDesigns(List<FavsDesigns> favoriteDesigns) {
        this.favoriteDesigns = favoriteDesigns;
    }
}
