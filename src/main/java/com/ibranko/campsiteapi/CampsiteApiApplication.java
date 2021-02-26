package com.ibranko.campsiteapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CampsiteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampsiteApiApplication.class, args);
    }

}
