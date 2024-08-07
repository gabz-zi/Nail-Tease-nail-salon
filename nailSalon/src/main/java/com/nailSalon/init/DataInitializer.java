package com.nailSalon.init;

import com.nailSalon.config.AdminConfig;
import com.nailSalon.model.entity.User;
import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.RoleRepository;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AdminConfig adminConfig;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, AdminConfig adminConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.adminConfig = adminConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Check if admin already exists
        if (userRepository.findByUsername(adminConfig.getUsername()).isEmpty()) {

            UserRoleEntity adminRole = roleRepository.findByName(RoleName.ADMIN);
            UserRoleEntity employeeRole = roleRepository.findByName(RoleName.EMPLOYEE);
            UserRoleEntity userRole = roleRepository.findByName(RoleName.USER);

            User adminUser = new User();
            adminUser.setUsername(adminConfig.getUsername());
            adminUser.setPassword(new BCryptPasswordEncoder().encode(adminConfig.getPassword()));
            adminUser.setEmail("gabriellla_koleva@abv.bg");
            adminUser.getRoles().add(adminRole);
            adminUser.getRoles().add(employeeRole);
            adminUser.getRoles().add(userRole);

            userRepository.save(adminUser);
        }
    }
}

