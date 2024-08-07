package nailSalon_services.service;

import nailSalon_services.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserClientService {

    private final RestTemplate restTemplate;

    @Value("${main.app.url}")
    private String mainAppUrl; // Base URL

    public UserClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UserDTO> fetchAllUsers() {
        try {
            String url = mainAppUrl + "/api/users/with-designs";
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(url, UserDTO[].class);

            // Check for null and empty body
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching users: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
