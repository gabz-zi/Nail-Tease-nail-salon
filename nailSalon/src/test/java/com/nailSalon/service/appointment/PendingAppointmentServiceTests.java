package com.nailSalon.service.appointment;

import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.PendingAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PendingAppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PendingAppointmentService pendingAppointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPendingAppointments() {
        // Arrange
        String username = "user1";
        Appointment appointment1 = createMockAppointment("user2", Category.GEL, false);
        Appointment appointment2 = createMockAppointment("user3", Category.GELISH, false);

        when(appointmentRepository.findAllByStatusAndUserUsernameNotAndCancelled(0, username, false))
                .thenReturn(List.of(appointment1, appointment2));

        // Act
        List<PendingAppointmentView> result = pendingAppointmentService.findAllPendingAppointments(username);

        // Assert
        verify(appointmentRepository, times(1)).findAllByStatusAndUserUsernameNotAndCancelled(0, username, false);
        assertEquals(2, result.size());
        assertEquals("user2", result.get(0).getCreateBy());
        assertEquals("user3", result.get(1).getCreateBy());
    }

    private Appointment createMockAppointment(String username, Category category, boolean cancelled) {
        Appointment appointment = new Appointment();
        appointment.setCreateOn(LocalDateTime.now().minusDays(1));
        appointment.setMadeFor(LocalDateTime.now().plusDays(1));

        User user = new User();
        user.setUsername(username);
        appointment.setUser(user);

        NailService service = new NailService();
        service.setCategory(category);
        service.setPrice(50.00);
        appointment.setService(service);
        appointment.setCancelled(cancelled);

        return appointment;
    }

    @Test
    void testAcceptAppointment() {
        // Arrange
        Long appointmentId = 1L;
        String username = "employee1";

        User employee = new User();
        employee.setUsername(username);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(0);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        // Act
        pendingAppointmentService.acceptAppointment(appointmentId, username);

        // Assert
        assertEquals(1, appointment.getStatus());
        assertEquals(employee, appointment.getTakenBy());
        assertTrue(employee.getAcceptedAppointments().contains(appointment));

        verify(appointmentRepository, times(1)).save(appointment);
        verify(userRepository, times(1)).save(employee);
    }

    @Test
    void testDeclineAppointment() {
        // Arrange
        Long appointmentId = 1L;
        String username = "employee1";

        User employee = new User();
        employee.setUsername(username);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(0);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        // Act
        pendingAppointmentService.declineAppointment(appointmentId, username);

        // Assert
        assertEquals(3, appointment.getStatus());
        assertEquals(employee, appointment.getTakenBy());

        verify(appointmentRepository, times(1)).save(appointment);
    }


}
