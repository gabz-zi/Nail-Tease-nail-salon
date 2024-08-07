package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;


public class AddDesignDTO {
    @NotNull
    @Size(min = 3, max = 40, message = "Name length must be between 5 and 40 characters!")
    private String name;


    @NotNull(message = "Please select category!")
    private String category;


    @NotNull(message = "Please upload an image!")
    private MultipartFile imageUrl;


    public AddDesignDTO() {

    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
    }
}
