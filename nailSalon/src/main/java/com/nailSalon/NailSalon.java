package com.nailSalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NailSalon {

    public static void main(String[] args) {
        SpringApplication.run(NailSalon.class, args);
    }
}
