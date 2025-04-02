package com.example.time.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TimeLoggerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeLoggerWebApplication.class, args);
    }
}