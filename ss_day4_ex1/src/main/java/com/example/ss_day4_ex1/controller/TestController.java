package com.example.ss_day4_ex1.controller;

import com.example.ss_day4_ex1.proxy.DemoProxy;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private final DemoProxy demoProxy;

    @GetMapping("/test")
    public String test() {
        return demoProxy.demo();
    }
}
