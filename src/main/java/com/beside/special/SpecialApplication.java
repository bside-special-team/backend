package com.beside.special;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpecialApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpecialApplication.class, args);
    }
}
