package com.nailSalon.service.appointment;

import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.NailServiceRepository;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteAppointmentTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private NailServiceRepository nailServiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService; // Updated service class

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        // Arrange
        Long appointmentId = 1L;

        User user = new User();
        user.setAppointments(new ArrayList<>());

        NailService service = new NailService();
        service.setAppointments(new ArrayList<>());

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setUser(user);
        appointment.setService(service);

        // Simulate appointment being taken by a user
        User takenBy = new User();
        takenBy.setAcceptedAppointments(new ArrayList<>());
        appointment.setTakenBy(takenBy);

        // Add appointment to lists
        user.getAppointments().add(appointment);
        service.getAppointments().add(appointment);
        takenBy.getAcceptedAppointments().add(appointment);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(nailServiceRepository.save(any(NailService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        appointmentService.delete(appointmentId);

        // Assert
        verify(appointmentRepository, times(1)).deleteById(appointmentId);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(takenBy);
        verify(nailServiceRepository, times(1)).save(service);

        // Verify the appointment has been removed from the user and service
        assertFalse(user.getAppointments().contains(appointment));
        assertFalse(service.getAppointments().contains(appointment));
        assertFalse(takenBy.getAcceptedAppointments().contains(appointment));
    }
}

