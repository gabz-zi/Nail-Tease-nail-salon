package com.nailSalon.service.user;

import com.nailSalon.model.entity.User;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
public class FindByUsernameTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername_UserExists() {

        String username = "existingUser";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findByUsername(username);


        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testFindByUsername_UserDoesNotExist() {

        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());


        User actualUser = userService.findByUsername(username);


        assertNull(actualUser);
    }
}
