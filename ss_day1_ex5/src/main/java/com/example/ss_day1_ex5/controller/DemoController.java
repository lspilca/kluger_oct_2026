package com.example.ss_day1_ex5.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DemoController {

    @ResponseBody
    @PostMapping("/test")
    public String demo() {
        return "Test";
    }

    @GetMapping("/")
    public String abc() {
        return "home.html";
    }
}
