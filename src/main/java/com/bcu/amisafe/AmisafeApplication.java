package com.bcu.amisafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AmisafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmisafeApplication.class, args);
    }

}
