package com.nailSalon.repository;

import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.ExperienceLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CVRepository extends JpaRepository<CV, Long> {
    Optional<CV> findByExperienceLevel(ExperienceLevel experienceLevel);

}
