package com.nailSalon.service;

import com.nailSalon.model.dto.AddAppointmentDTO;
import com.nailSalon.model.dto.ApplicationFormDTO;
import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.CVRepository;
import com.nailSalon.repository.UserRepository;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CVService {

    private final CVRepository cvRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public CVService(CVRepository cvRepository, UserService userService, UserRepository userRepository) {
        this.cvRepository = cvRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transient
    @Transactional
    public void addApplicationFormToUserWithUsername(ApplicationFormDTO applicationFormDTO, String username) {
        User user = userService.findByUsername(username);
        CV cv = new CV();
        cv.setUser(user);
        cv.setExperienceLevel(applicationFormDTO.getExperienceLevel());
        cv.setPersonalMotivation(applicationFormDTO.getPersonalMotivation());
        cvRepository.save(cv);
        user.setCv(cv);
        userRepository.save(user);
    }

}
