package com.nailSalon.model.dto;

import com.nailSalon.model.entity.CV;

public class ApplicantDTO {

    private long id;
    private String username;
    private String email;
    private CV cv;

    public ApplicantDTO() {
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }
}
