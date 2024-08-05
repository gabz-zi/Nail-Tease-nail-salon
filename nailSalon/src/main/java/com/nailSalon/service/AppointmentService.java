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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        appointment.setCancelled(false);
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
        List<Appointment> appointments = appointmentRepository.findAllByUserUsernameAndCancelled(username, false);
        for (Appointment appointment : appointments) {
            MyAppointmentView myAppointmentView = appointmentToMyAppointment(appointment, new MyAppointmentView());
            myAppointmentViewList.add(myAppointmentView);
        }
        myAppointmentViewList.sort(Comparator.comparing(MyAppointmentView::getMadeFor));
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


    @Transactional //scheduled method
    public void deleteRejectedAndCancelledAppointmentsAndTrackEmployees() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX); // End of the day (23:59:59)

        List<Appointment> rejectedAppointments = new ArrayList<>();
        List<Appointment> cancelledAppointments = new ArrayList<>();

        rejectedAppointments = appointmentRepository.findAllByStatusAndCreateOnBetween(3, startOfDay, endOfDay);
        Map<User, Long> declinedCounts = rejectedAppointments.stream()
                .collect(Collectors.groupingBy(Appointment::getTakenBy, Collectors.counting()));

        for (Appointment rejected : rejectedAppointments) {
            delete(rejected.getId());
        }

        cancelledAppointments = appointmentRepository.findAllByCancelled(true);
        for (Appointment cancelled : cancelledAppointments) {
            delete(cancelled.getId());
        }

        for (Map.Entry<User, Long> entry : declinedCounts.entrySet()) {
            User employee = entry.getKey();
            Long count = entry.getValue();
            if (count >= 5) {
                employee.setIsBanned(true);
                userRepository.save(employee);
            }
        }
    }

    // SINCE IT'S OPTIONAL THERE MIGHT EB A NEED FOR CHECKING "ORELSE THROW"
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).get();
    }

    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }
}
