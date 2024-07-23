package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.Design;

public class MyDesignsDTO {
    private long id;
    String imageUrl;
    String name;
    Category category;

    public MyDesignsDTO(Design design) {
        this.imageUrl = design.getImageUrl();
        this.name = design.getName();
        this.category = design.getCategory();
        this.id = design.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
