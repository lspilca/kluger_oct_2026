package com.example.ss_day1_ex4.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

//    @CrossOrigin("http://localhost:8080")
    @PostMapping("/demo")
    public String demo() {
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        return pe.encode("Demo");
    }

    @PostMapping("/abc")
    public String abc() {
        return "ABC";
    }
}
