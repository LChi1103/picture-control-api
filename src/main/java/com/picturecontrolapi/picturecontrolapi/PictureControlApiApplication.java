package com.picturecontrolapi.picturecontrolapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PictureControlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureControlApiApplication.class, args);
    }

}
