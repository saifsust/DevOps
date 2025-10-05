package com.techview.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.techview.*"})
public class RiderApp {

    public static void main(String[] args) {
        SpringApplication.run(RiderApp.class, args);
    }
}
