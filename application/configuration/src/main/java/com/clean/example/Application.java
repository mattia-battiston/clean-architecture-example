package com.clean.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.clean.example.configuration")
public class Application {

    int variable = 0;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}