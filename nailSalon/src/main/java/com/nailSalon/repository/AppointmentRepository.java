package com.nailSalon.repository;

import com.nailSalon.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByUserUsername(String username);

    Optional<Appointment> findById(Long id);
}
