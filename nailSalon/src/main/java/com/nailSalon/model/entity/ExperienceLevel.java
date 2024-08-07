package com.nailSalon.model.entity;

public enum ExperienceLevel {
    LEARNER("➤ I have undergone several nail artistry courses and am eager to put the learned into practise."),
    INTERN("➤ Still building knowledge but have found it helpful to be in a real work environment."),
    BEGINNER("➤ I have been practising the profession for about a year."),
    INTERMEDIATE("➤ I have been working in the beauty industry for 1-2 years."),
    ADVANCED("➤ 3-4 years work experience as nail technician."),
    EXPERT("➤ 5+ years working in that field leading to strong foundation now focusing on new upgrading courses.");

    private final String displayName;

    ExperienceLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
