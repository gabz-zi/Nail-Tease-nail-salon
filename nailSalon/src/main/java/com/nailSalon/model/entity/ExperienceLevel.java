package com.nailSalon.model.entity;

public enum ExperienceLevel {
    LEARNER("Learner"),
    INTERN("Intern"),
    EMPLOYEE_BEGINNER("Employee Beginner"),
    EMPLOYEE_INTERMEDIATE("Employee Intermediate"),
    EMPLOYEE_ADVANCED("Employee Advanced"),
    EMPLOYEE_EXPERT("Employee Expert");

    private final String displayName;

    ExperienceLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
