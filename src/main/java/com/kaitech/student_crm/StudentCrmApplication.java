package com.kaitech.student_crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentCrmApplication.class, args);
    }

    @GetMapping("helloWorld")
    public String hello() {
        return "Hello World, it's your project CRM";
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }
}