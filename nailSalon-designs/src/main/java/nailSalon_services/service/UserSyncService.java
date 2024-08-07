package nailSalon_services.service;

import nailSalon_services.model.dto.UserDTO;
import nailSalon_services.model.entity.User;
import nailSalon_services.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSyncService {

    private final UserClientService userClientService;
    private final UserRepository userRepository;

    public UserSyncService(UserClientService userClientService, UserRepository userRepository) {
        this.userClientService = userClientService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void synchronizeUsers() {
        List<UserDTO> userDTOs = userClientService.fetchAllUsers();
        for (UserDTO userDTO : userDTOs) {
            Optional<User> existingUser = userRepository.findById(userDTO.getId());
            if (existingUser.isEmpty()) {
                User user = new User();
                user.setId(userDTO.getId());
                user.setUsername(userDTO.getUsername());
                userRepository.save(user);
            }
        }
    }
}
