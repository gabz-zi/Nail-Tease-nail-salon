package com.nailSalon.service;

import com.nailSalon.model.dto.UserRegisterDTO;
import com.nailSalon.model.entity.Design;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.RoleRepository;
import com.nailSalon.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public boolean register(UserRegisterDTO data) {
        Optional<User> existingUser = userRepository
                .findByUsernameOrEmail(data.getUsername(), data.getEmail());

        if (existingUser.isPresent()) {
            return false;
        }

        User user = new User();

        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        UserRoleEntity userRole = roleRepository.findByName(RoleName.USER);
        user.getRoles().add(userRole);

        this.userRepository.save(user);

        return true;
    }

   /* public boolean login(UserLoginDTO data) {
        Optional<User> byUsername = userRepository.findByUsername(data.getUsername());

        if (byUsername.isEmpty()) {
            return false;
        }

        boolean passMatch = passwordEncoder
                .matches(data.getPassword(), byUsername.get().getPassword());

        if (!passMatch) {
            return false;
        }

        userSession.login(byUsername.get().getId(), data.getUsername());

        return true;
    }*/


    public User findUserById(long id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


}
