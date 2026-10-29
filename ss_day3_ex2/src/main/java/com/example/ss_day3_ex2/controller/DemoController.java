package com.example.ss_day3_ex2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "Demo!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/ciao")
    @PreAuthorize("hasRole('ADMIN')")
    public String ciao() {
        return "Ciao!";
    }
}
