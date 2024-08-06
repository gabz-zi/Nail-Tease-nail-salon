package com.nailSalon.model.dto;

import com.nailSalon.model.entity.ExperienceLevel;
import jakarta.validation.constraints.NotNull;

public class ApplicationFormDTO /*cv dto*/ {

    @NotNull(message = "Please select experience level!")
    private ExperienceLevel experienceLevel;
    private String personalMotivation;


    public ApplicationFormDTO() {
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getPersonalMotivation() {
        return personalMotivation;
    }

    public void setPersonalMotivation(String personalMotivation) {
        this.personalMotivation = personalMotivation;
    }
}