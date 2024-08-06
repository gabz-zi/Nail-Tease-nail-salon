package com.nailSalon.service;

import com.nailSalon.model.dto.ApplicantDTO;
import com.nailSalon.model.dto.ApplicationFormDTO;
import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.model.view.TodaysAppointmentView;
import com.nailSalon.repository.CVRepository;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.repository.UserRoleRepository;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicantService {

    private final CVRepository cvRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    public ApplicantService(CVRepository cvRepository, UserService userService, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.cvRepository = cvRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transient
    @Transactional
    public boolean addApplicationFormToUserWithUsername(ApplicationFormDTO applicationFormDTO, String username) {
        User user = userService.findByUsername(username);
        boolean successful;
         if (user.getCv() != null) {
             successful = false;
         } else {
        CV cv = new CV();
        cv.setUser(user);
        cv.setExperienceLevel(applicationFormDTO.getExperienceLevel());
        cv.setPersonalMotivation(applicationFormDTO.getPersonalMotivation());
        cvRepository.save(cv);
        user.setCv(cv);
        userRepository.save(user);
         successful = true;
         }
         return successful;
    }


    public List<ApplicantDTO> findAllApplicants() {

        List<User> applicants = userRepository.findAllByCvNotNull();

        return applicants.stream()
                .map(this::userToApplicantDTO)
                .collect(Collectors.toList());
    }

    private ApplicantDTO userToApplicantDTO(User user) {
        ApplicantDTO dto = new ApplicantDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCv(user.getCv());
        return dto;
    }

    @Transactional
    public void hireUser(Long id) {
        User employeeToBe = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserRoleEntity employeeRole = userRoleRepository.findByName(RoleName.EMPLOYEE)
                .orElseThrow(() -> new RuntimeException("Role EMPLOYEE not found"));
        employeeToBe.getRoles().add(employeeRole);
        employeeToBe.setCv(null);
        userRepository.save(employeeToBe);
    }

    public void rejectUser(Long id) {
        User rejectedUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        rejectedUser.setCv(null);
        userRepository.save(rejectedUser);
    }
}
