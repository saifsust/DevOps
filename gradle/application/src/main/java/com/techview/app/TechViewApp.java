package com.techview.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.techview.*"})
public class TechViewApp {

    public static void main(String[] args) {
        SpringApplication.run(TechViewApp.class, args);
    }
}
