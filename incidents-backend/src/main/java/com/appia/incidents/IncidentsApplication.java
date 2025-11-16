package com.appia.incidents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IncidentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(IncidentsApplication.class, args);
    }
}
