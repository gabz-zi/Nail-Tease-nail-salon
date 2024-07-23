package com.nailSalon.model.dto;

import com.nailSalon.model.entity.Design;

public class FavsDesigns {
    private long id;
    private String imageUrl;
    private String name;
    private String author;
    private String madeBy;

    public FavsDesigns(Design design) {
        this.imageUrl = design.getImageUrl();
        this.name = design.getName();
        this.madeBy = design.getMadeBy().getUsername();
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }
}
