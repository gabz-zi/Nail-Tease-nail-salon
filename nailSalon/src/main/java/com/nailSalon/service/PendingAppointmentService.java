package com.nailSalon.service;

import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.NailServiceRepository;
import com.nailSalon.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PendingAppointmentService {

    private final UserService userService;
    private final NailServiceService nailService;
    private final NailServiceRepository nailServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public PendingAppointmentService(UserService userService, NailServiceService nailService, NailServiceRepository nailServiceRepository, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.nailService = nailService;
        this.nailServiceRepository = nailServiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public List<PendingAppointmentView> findAllPendingAppointments(String username) {
        List<PendingAppointmentView> pendingAppointmentViewList = new ArrayList<>();
        List<Appointment> appointments = appointmentRepository.findAllByStatusAndUserUsernameNot(0, username);
        for (Appointment appointment : appointments) {
            PendingAppointmentView pendingAppointment = appointmentToPendingAppointment(appointment, new PendingAppointmentView());
            pendingAppointmentViewList.add(pendingAppointment);
        }
        return pendingAppointmentViewList;
    }

    public PendingAppointmentView appointmentToPendingAppointment(Appointment appointment, PendingAppointmentView pendingAppointment) {
        pendingAppointment.setCreateOn(appointment.getCreateOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")));
        pendingAppointment.setMadeFor(appointment.getMadeFor().format(DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")));
        pendingAppointment.setService(appointment.getService().getCategory().name().toLowerCase());
        pendingAppointment.setPrice(String.format("€ " + appointment.getService().getPriceFormatted()));
        pendingAppointment.setId(appointment.getId());
        pendingAppointment.setCreateBy(appointment.getUser().getUsername());
        return pendingAppointment;
    }

    @Transactional
    public void acceptAppointment(Long id, String username) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id:" + id));
        User employee = userRepository.findByUsername(username).get();
        appointment.setTakenBy(employee);
        employee.getAcceptedAppointments().add(appointment);
        appointment.setStatus(1);
        appointmentRepository.save(appointment);
        userRepository.save(employee);
    }


    @Transactional
    public void declineAppointment(Long id, String username) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
        appointment.setStatus(3);
        User employeeWhoDeclined = userRepository.findByUsername(username).get();
        appointment.setTakenBy(employeeWhoDeclined);
        appointmentRepository.save(appointment);
    }
}
