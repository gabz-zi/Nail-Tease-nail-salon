package com.nailSalon.service.applicant;

import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.repository.UserRoleRepository;
import com.nailSalon.service.ApplicantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HireUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private ApplicantService applicantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHireUser() {
        // Arrange
        Long userId = 1L;
        User userToBeHired = new User();
        userToBeHired.setId(userId);
        userToBeHired.setCv(new CV());

        UserRoleEntity employeeRole = new UserRoleEntity();
        employeeRole.setName(RoleName.EMPLOYEE);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(userToBeHired));
        when(userRoleRepository.findByName(RoleName.EMPLOYEE)).thenReturn(java.util.Optional.of(employeeRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock saving User

        // Act
        applicantService.hireUser(userId);

        // Assert
        assertTrue(userToBeHired.getRoles().contains(employeeRole));
        assertNull(userToBeHired.getCv()); // CV should be removed
    }
}
