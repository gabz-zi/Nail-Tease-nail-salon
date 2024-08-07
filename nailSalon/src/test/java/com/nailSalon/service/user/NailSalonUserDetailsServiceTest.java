package com.nailSalon.service.user;


import com.nailSalon.model.NailSalonUserDetails;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.NailSalonUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NailSalonUserDetailsServiceTest {
    private static final String TEST_USERNAME = "gabi";
    private static final String NONEXISTENT_USERNAME = "none";

    private NailSalonUserDetailsService toTest;
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new NailSalonUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsernameSuccessful() {

        // Arrange
        User testUser = new User();
                testUser.setEmail("gabz@abv.bg");
                testUser.setPassword("topsecret");
                testUser.setUsername(TEST_USERNAME);
                testUser.setRoles(List.of(
                        new UserRoleEntity().setName(RoleName.ADMIN),
                        new UserRoleEntity().setName(RoleName.USER)
                ));

        when(mockUserRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = toTest.loadUserByUsername(TEST_USERNAME);

        Assertions.assertInstanceOf(NailSalonUserDetails.class, userDetails);

        NailSalonUserDetails nailSalonUserDetails = (NailSalonUserDetails) userDetails;

        Assertions.assertEquals(TEST_USERNAME, userDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUser.getUsername(), nailSalonUserDetails.getUsername());


        var expectedRoles = testUser.getRoles().stream().map(UserRoleEntity::getName).map(r -> "ROLE_" + r).toList();
        var actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername(NONEXISTENT_USERNAME)
        );
    }
}
