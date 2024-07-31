package com.nailSalon.service;

import com.nailSalon.model.dto.AddAppointmentDTO;
import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class AppointmentService {

    private final UserService userService;
    private final NailServiceService nailService;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(UserService userService, NailServiceService nailService, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.nailService = nailService;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Transient
    @Transactional
    public void addAppointmentToUserWithUsername(AddAppointmentDTO addAppointmentDTO, String username) {
        User user = userService.findByUsername(username);
        Appointment appointment = new Appointment();
        appointment.setMadeFor(addAppointmentDTO.getMadeFor());
        appointment.setCreateOn(LocalDateTime.now());
        appointment.setStatus(0);
        appointment.setUser(user);
        NailService service = nailService.getByName(addAppointmentDTO.getService());
        service.getAppointments().add(appointment);
        appointment.setService(service);
        appointmentRepository.save(appointment);
        user.getAppointments().add(appointment);
        userRepository.save(user);
    }
}
