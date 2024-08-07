package com.nailSalon.config;

import com.nailSalon.model.enums.RoleName;
import com.nailSalon.repository.UserRepository;
import com.nailSalon.service.NailSalonUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/", "/login", "/register", "/about-us", "/services", "/gallery").permitAll()
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/pending-appointments", "/todays-appointments", "/add-design", "appointments/accept/", "/appointments/decline/").hasRole(RoleName.EMPLOYEE.name())
                                .requestMatchers("/users/fire/", "/users/hire/","/add-service", "/applicants", "/banned-employees", "/services/remove/", "/designs/remove/").hasRole(RoleName.ADMIN.name())
                                .requestMatchers("/appointments/remove/", "/apply-job").hasRole(RoleName.USER.name())
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/my-appointments", true)
                                .failureUrl("/login/error")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public UserDetailsService salonUserDetailsService(UserRepository userRepository) {
        return new NailSalonUserDetailsService(userRepository);
    }

}