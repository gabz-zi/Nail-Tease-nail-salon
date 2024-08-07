package com.nailSalon.service.applicant;

import com.nailSalon.model.dto.ApplicationFormDTO;
import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.ExperienceLevel;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.CVRepository;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.repository.UserRoleRepository;
import com.nailSalon.service.ApplicantService;
import com.nailSalon.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AddApplicationToUserTest {

    @Mock
    private CVRepository cvRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ApplicantService applicantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddApplicationFormToUserWithUsername_NoCV() {
        // Arrange
        String username = "testUser";
        ApplicationFormDTO applicationFormDTO = new ApplicationFormDTO();
        applicationFormDTO.setExperienceLevel(ExperienceLevel.EXPERT);
        applicationFormDTO.setPersonalMotivation("Motivated to work");

        User user = new User();
        user.setCv(null); // User does not have a CV

        when(userService.findByUsername(username)).thenReturn(user);
        when(cvRepository.save(any(CV.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock saving CV
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock saving User

        // Act
        boolean result = applicantService.addApplicationFormToUserWithUsername(applicationFormDTO, username);

        // Assert
        assertTrue(result);
    }

    @Test
    void testAddApplicationFormToUserWithUsername_ExistingCV() {
        // Arrange
        String username = "testUser";
        ApplicationFormDTO applicationFormDTO = new ApplicationFormDTO();
        applicationFormDTO.setExperienceLevel(ExperienceLevel.EXPERT);
        applicationFormDTO.setPersonalMotivation("Motivated to work");

        CV existingCV = new CV();
        User user = new User();
        user.setCv(existingCV); // User already has a CV

        when(userService.findByUsername(username)).thenReturn(user);

        // Act
        boolean result = applicantService.addApplicationFormToUserWithUsername(applicationFormDTO, username);

        // Assert
        assertFalse(result);
    }
}

