package com.kubernetes.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kubernetes.*"})
public class k8sApplication {

    public static void main(String[] args) {
        SpringApplication.run(k8sApplication.class, args);
    }
}