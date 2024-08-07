package com.nailSalon.controller;

import com.nailSalon.model.dto.UserDTO;
import com.nailSalon.model.entity.User;
import com.nailSalon.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/with-designs")
    public ResponseEntity<List<UserDTO>> getUsersWithDesigns() {
        List<User> usersWithDesigns = userRepository.findAll();
        List<UserDTO> userDTOs = usersWithDesigns.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
}
