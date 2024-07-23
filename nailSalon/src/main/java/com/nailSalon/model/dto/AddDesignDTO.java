package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddDesignDTO {
    @NotNull
    @Size(min = 5, max = 40, message = "Name length must be between 5 and 40 characters!")
    private String name;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @NotNull(message = "Please select category!")
    private Category category;


    @NotNull
    @NotBlank(message = "Please enter valid image URL!")
    private String imageUrl;


    public AddDesignDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
