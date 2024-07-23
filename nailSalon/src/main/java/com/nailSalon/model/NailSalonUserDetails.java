package com.nailSalon.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class NailSalonUserDetails extends User {
    public NailSalonUserDetails(String username, String password,
         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
