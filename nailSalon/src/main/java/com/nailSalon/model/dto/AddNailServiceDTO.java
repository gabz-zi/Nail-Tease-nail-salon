package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Category;


import javax.validation.constraints.*;

public class AddNailServiceDTO {

    @NotNull
    @Size(min = 5, max = 40, message = "Name length must be between 5 and 100 characters!")
    private String name;

    @NotNull(message = "You must select a duration!")
    @NotBlank(message = "You must select a duration!")
    private String duration;

    @NotNull
    @Positive(message = "Please enter valid price!")
    private double price;
    @NotNull(message = "You must select a category!")
    private Category category;

    private String description;

    public AddNailServiceDTO() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
