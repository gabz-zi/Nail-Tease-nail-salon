package com.nailSalon.service.appointment;

import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.view.TodaysAppointmentView;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.service.TodaysAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FindAllForTodayTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private TodaysAppointmentService todaysAppointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllAppointmentsForToday() {
        String username = "testUser";
        LocalDate today = LocalDate.now();
        LocalDateTime appointmentTime1 = LocalDateTime.of(today, LocalTime.of(10, 0));
        LocalDateTime appointmentTime2 = LocalDateTime.of(today, LocalTime.of(14, 0));

        User user = new User();
        user.setUsername(username);

        NailService service = new NailService();
        service.setDuration("30");
        service.setCategory(Category.PEDICURE);
        service.setPrice(20);

        Appointment appointment1 = new Appointment();
        appointment1.setUser(user);
        appointment1.setMadeFor(appointmentTime1);
        appointment1.setService(service);

        Appointment appointment2 = new Appointment();
        appointment2.setUser(user);
        appointment2.setMadeFor(appointmentTime2);
        appointment2.setService(service);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        when(appointmentRepository.findAllByTakenBy_UsernameAndStatus(username, 1)).thenReturn(appointments);

        List<TodaysAppointmentView> result = todaysAppointmentService.findAllAppointmentsForToday(username);

        assertEquals(2, result.size());
        assertEquals("at 10:00 on 07.08", result.get(0).getTime());
        assertEquals("at 14:00 on 07.08", result.get(1).getTime());
        assertEquals("testUser", result.get(0).getCreateBy());
        assertEquals("testUser", result.get(1).getCreateBy());
        assertEquals("PEDICURE", result.get(0).getService());
        assertEquals("PEDICURE", result.get(1).getService());
        assertEquals("30", result.get(0).getDuration());
        assertEquals("30", result.get(1).getDuration());
        assertEquals("20", result.get(0).getPrice());
        assertEquals("20", result.get(1).getPrice());
    }
}
