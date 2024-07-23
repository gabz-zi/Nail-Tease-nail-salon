package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.Design;

public class OtherDesignsDTO {

    String imageUrl;
    String name;
    Category category;

    long id;
    String madeBy; //addedBy

    public OtherDesignsDTO(Design design) {
        this.id = design.getId();
        this.imageUrl = design.getImageUrl();
        this.name = design.getName();
        this.category = design.getCategory();
        this.madeBy = design.getMadeBy().getUsername();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }

    public long getId() {
        return id;
    }
}
