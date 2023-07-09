package com.university.studentcourseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StudentCourseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentCourseServiceApplication.class, args);
    }

}
