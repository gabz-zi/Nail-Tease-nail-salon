package com.nailSalon.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CVs")
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @Column(nullable = false)
    private String description;

    @Column
    private String personalMotivation;

    @OneToOne(mappedBy = "cv")
    private User user;

    public CV() {
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
        this.setDescription(experienceLevel);
    }

    public void setDescription(ExperienceLevel experienceLevel) {
        String description = "";
        switch (experienceLevel) {
            case LEARNER ->
                    description = "I have undergone several nail artistry courses and am eager to put the learned into practise.";
            case INTERN ->
                    description = "Still building knowledge but have found it helpful to be in a real work environment where I was given small but meaningful tasks.";
            case BEGINNER ->
                    description = "I have been practising the profession for about a year.";
            case INTERMEDIATE ->
                    description = "I have been working in the beauty industry for 1-2 years.";
            case ADVANCED ->
                    description = "3-4 years work experience as nail technician.";
            case EMPLOYEE_EXPERT ->
                    description = "5+ years working in that field leading to strong foundation providing me with more time for new upgrading courses to expand and refine my skills.";
        }
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public String getDescription() {
        return description;
    }


    public String getPersonalMotivation() {
        return personalMotivation;
    }

    public void setPersonalMotivation(String personalMotivation) {
        this.personalMotivation = personalMotivation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

