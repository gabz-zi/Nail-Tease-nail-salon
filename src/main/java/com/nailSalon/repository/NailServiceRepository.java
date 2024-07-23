package com.nailSalon.repository;

import com.nailSalon.model.entity.NailService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NailServiceRepository extends JpaRepository<NailService, Long> {

}
