package com.nailSalon.service;

import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.view.TodaysAppointmentView;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.NailServiceRepository;
import com.nailSalon.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodaysAppointmentService {

    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public TodaysAppointmentService(UserService userService, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public List<TodaysAppointmentView> findAllAppointmentsForToday(String username) {

        LocalDate today = LocalDate.now();

        List<Appointment> appointments = appointmentRepository.findAllByTakenBy_UsernameAndStatus(username, 1);

        return appointments.stream()
                .filter(appointment -> appointment.getMadeFor().toLocalDate().equals(today))
                .sorted(Comparator.comparing(appointment -> appointment.getMadeFor().toLocalTime()))
                .map(this::appointmentToTodaysAppointment)
                .collect(Collectors.toList());
    }


    private TodaysAppointmentView appointmentToTodaysAppointment(Appointment appointment) {
        TodaysAppointmentView view = new TodaysAppointmentView();
        view.setCreateBy(appointment.getUser().getUsername());
        view.setTime(appointment.getMadeFor());
        view.setDuration(appointment.getService().getDuration());
        view.setService(appointment.getService().getCategory().name());
        view.setPrice(appointment.getService().getPriceFormatted());
        return view;
    }
}
