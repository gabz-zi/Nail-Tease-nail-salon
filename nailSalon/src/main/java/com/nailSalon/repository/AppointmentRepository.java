package com.nailSalon.repository;

import com.nailSalon.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByUserUsername(String username);

    Optional<Appointment> findById(Long id);

    List<Appointment> findAllByStatusAndUserUsernameNot(Integer number, String username);

    List<Appointment> findAllByTakenBy_UsernameAndStatus(String username, int i);

    List<Appointment> findAllByStatusAndCreateOnBetween(Integer status, LocalDateTime start, LocalDateTime end);

    List<Appointment> findAllByStatus(int i);

    List<Appointment> findAllByUserUsernameAndStatusNot(String username, int i);

    List<Appointment> findAllByUserUsernameAndCancelled(String username, boolean cancelled);

    List<Appointment> findAllByStatusAndUserUsernameNotAndCancelled(int i, String username, boolean b);

    List<Appointment> findAllByCancelled(boolean b);
}
