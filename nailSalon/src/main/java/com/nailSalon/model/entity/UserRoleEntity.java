package com.nailSalon.model.entity;

import com.nailSalon.model.enums.RoleName;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "roles")
public class UserRoleEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;


    public UserRoleEntity() {
    }

    public UserRoleEntity(Long id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UserRoleEntity setId(Long id) {
        this.id = id;
        return this;
    }


    public UserRoleEntity setName(RoleName name) {
        this.name = name;
        return this;
    }

    public RoleName getName() {
        return name;
    }
}