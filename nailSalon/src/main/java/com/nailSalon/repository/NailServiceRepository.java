package com.nailSalon.repository;

import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.NailService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NailServiceRepository extends JpaRepository<NailService, Long> {

     List<NailService> findAllByCategory(Category cat);
}
