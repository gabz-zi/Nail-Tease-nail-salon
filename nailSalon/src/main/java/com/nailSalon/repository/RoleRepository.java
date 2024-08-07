package com.nailSalon.repository;

import com.nailSalon.model.entity.UserRoleEntity;
import com.nailSalon.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByName(RoleName name);
}
