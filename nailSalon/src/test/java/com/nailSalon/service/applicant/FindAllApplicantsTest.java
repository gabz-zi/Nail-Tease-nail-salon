package com.nailSalon.service.applicant;

import com.nailSalon.model.dto.ApplicantDTO;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FindAllApplicantsTest {

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
    void testFindAllApplicants() {
        // Arrange
        CV cv1 = new CV();
        CV cv2 = new CV();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setCv(cv1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setCv(cv2);

        List<User> usersWithCV = Arrays.asList(user1, user2);

        when(userRepository.findAllByCvNotNull()).thenReturn(usersWithCV);

        // Act
        List<ApplicantDTO> applicantDTOs = applicantService.findAllApplicants();

        // Assert
        assertEquals(2, applicantDTOs.size());

        ApplicantDTO dto1 = applicantDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("user1", dto1.getUsername());
        assertEquals("user1@example.com", dto1.getEmail());
        assertEquals(cv1, dto1.getCv());

        ApplicantDTO dto2 = applicantDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("user2", dto2.getUsername());
        assertEquals("user2@example.com", dto2.getEmail());
        assertEquals(cv2, dto2.getCv());
    }
}
