package com.nailSalon.service.user;

import com.nailSalon.model.dto.UserRegisterDTO;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.repository.RoleRepository;
import com.nailSalon.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Arrange
        UserRegisterDTO data = new UserRegisterDTO();
        data.setUsername("existingUser");
        data.setEmail("email@example.com");
        data.setPassword("password");


        when(userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail()))
                .thenReturn(Optional.of(new User()));

        // Act
        boolean result = userService.register(data);

        // Assert
        assertFalse(result);
        Mockito.verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testRegister_NewUser() {
        // Arrange
        UserRegisterDTO data = new UserRegisterDTO("newUser", "newEmail@example.com", "password");
        when(userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail()))
                .thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.USER))
                .thenReturn(new UserRoleEntity(1L, RoleName.USER));
        when(passwordEncoder.encode(data.getPassword()))
                .thenReturn("encodedPassword");

        // Act
        boolean result = userService.register(data);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
