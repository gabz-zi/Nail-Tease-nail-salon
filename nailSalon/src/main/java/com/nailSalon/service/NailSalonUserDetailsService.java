package com.nailSalon.service;

import com.nailSalon.model.NailSalonUserDetails;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.enums.UserRoleEnum;
import com.nailSalon.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class NailSalonUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public NailSalonUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

              return userRepository
                        .findByUsername(username)
                        .map(NailSalonUserDetailsService::map)
                        .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private static GrantedAuthority map(UserRoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }

    private static UserDetails map(User user) {
        return new NailSalonUserDetails(
                user.getUsername(),
                user.getPassword(),
                List.of() /*TODO*/
        );
    }


}
