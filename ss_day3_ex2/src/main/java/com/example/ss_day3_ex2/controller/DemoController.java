package com.example.ss_day3_ex2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    private String demo() {
        return "Demo!";
    }
}
