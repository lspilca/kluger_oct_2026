package com.example.ss_day3_ex2.unit;

import com.example.ss_day3_ex2.controller.DemoController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CiaoUnitTests {

    @Test
    public void test1(){
        DemoController demoController = new DemoController();

        String result = demoController.ciao();

        assertEquals("Ciao!", result);
    }
}
