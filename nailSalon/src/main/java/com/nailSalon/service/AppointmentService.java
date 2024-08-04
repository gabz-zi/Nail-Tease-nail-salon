package com.nailSalon.service;

import com.nailSalon.model.dto.AddAppointmentDTO;
import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.view.MyAppointmentView;
import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.NailServiceRepository;
import com.nailSalon.repository.UserRepository;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final UserService userService;
    private final NailServiceService nailService;
    private final NailServiceRepository nailServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(UserService userService, NailServiceService nailService, NailServiceRepository nailServiceRepository, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.nailService = nailService;
        this.nailServiceRepository = nailServiceRepository;
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

    public List<MyAppointmentView> getAppointmentsOfUser(String username) {
        List<MyAppointmentView> myAppointmentViewList = new ArrayList<>();
        List<Appointment> appointments = appointmentRepository.findAllByUserUsername(username);
        for (Appointment appointment : appointments) {
            System.out.println(appointment.getService().getName());
            MyAppointmentView myAppointmentView = appointmentToMyAppointment(appointment, new MyAppointmentView());
            myAppointmentViewList.add(myAppointmentView);
        }
        return myAppointmentViewList;
    }

    public MyAppointmentView appointmentToMyAppointment(Appointment appointment, MyAppointmentView myAppointmentView) {
        myAppointmentView.setCreateOn(appointment.getCreateOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")));
        myAppointmentView.setMadeFor(appointment.getMadeFor().format(DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")));
        myAppointmentView.setService(appointment.getService().getCategory().name().toLowerCase());
        myAppointmentView.setStatus(appointment.getStatus() == 0 ? "PENDING" : appointment.getStatus() == 1 ? "APPROVED" : "REJECTED");
        myAppointmentView.setPrice(String.format("€ " + appointment.getService().getPriceFormatted()));
        myAppointmentView.setId(appointment.getId());
        return myAppointmentView;
    }

    @Transactional  // throws lazily alabala exception so transactional
    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id).get();
        appointment.getUser().getAppointments().remove(appointment);
        appointment.getService().getAppointments().remove(appointment);
        if (appointment.getTakenBy() != null) {
            appointment.getTakenBy().getAcceptedAppointments().remove(appointment);
            userRepository.save(appointment.getTakenBy());
            appointment.setTakenBy(null);
        }
        userRepository.save(appointment.getUser());
        nailServiceRepository.save(appointment.getService());
        appointment.setUser(null);
        appointment.setService(null);
        appointmentRepository.deleteById(id);
    }


}
