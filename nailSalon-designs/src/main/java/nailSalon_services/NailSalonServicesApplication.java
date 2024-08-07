package nailSalon_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NailSalonServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(NailSalonServicesApplication.class, args);
	}

}
