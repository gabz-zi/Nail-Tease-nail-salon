package com.nailSalon.service.applicant;

import com.nailSalon.model.entity.CV;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.repository.CVRepository;
import com.nailSalon.repository.UserRoleRepository;
import com.nailSalon.service.ApplicantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class RejectApplicantTest {

    @Mock
    private CVRepository cvRepository;

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
    void testRejectUser() {
        // Arrange
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        CV cv = new CV();
        user.setCv(cv);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        // Act
        applicantService.rejectUser(userId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        assertNull(user.getCv(), "The CV should be removed from the user");
    }
}
