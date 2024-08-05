package com.nailSalon.config;

import com.nailSalon.service.AppointmentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerConfig {

    private final AppointmentService appointmentService;

    public SchedulerConfig(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "0 59 23 * * ?")  // Runs at 23:59 every day    "0 59 23 * * ?"
    // IMPORTANT: TO TEST IT YOU CAN USE THAT CRON "0 */5 * * * ?" = EVERY 5 MINUTES
    public void deleteDeclinedAppointmentsAndTrackEmployees() {
        appointmentService.deleteRejectedAndCancelledAppointmentsAndTrackEmployees();
    }
}

