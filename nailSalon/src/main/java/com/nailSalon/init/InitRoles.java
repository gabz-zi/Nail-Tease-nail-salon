package com.nailSalon.init;

import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class InitRoles implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public InitRoles(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count() == 0) {
            List<UserRoleEntity> roles = new ArrayList<>();

            Arrays.stream(RoleName.values())
                    .forEach(roleName -> {
                        UserRoleEntity role = new UserRoleEntity();
                        role.setName(roleName);
                        roles.add(role);
                    });
            roleRepository.saveAll(roles);
        }
    }

}
