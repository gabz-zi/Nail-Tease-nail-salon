package com.nailSalon.repository;

import com.nailSalon.model.entity.Style;
import com.nailSalon.model.entity.StyleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
    Optional<Style> findByName(StyleName style);
}
