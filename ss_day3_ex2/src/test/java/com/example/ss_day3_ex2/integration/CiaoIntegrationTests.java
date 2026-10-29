package com.example.ss_day3_ex2.integration;

import com.example.ss_day3_ex2.controller.DemoController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CiaoIntegrationTests {

    @Autowired
    private DemoController demoController;

    @Test
    @DisplayName("""
            GIVEN endpoint /ciao is called
            WHEN no authentication is found
            THEN method call fails with an authentication exception
            """)
    public void test1() {

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> demoController.ciao()
        );
    }

    @Test
    @DisplayName("""
            GIVEN endpoint /ciao is called
            WHEN authentication is provided 
            AND authentication hasn't the correct role
            THEN method call fails with an authorization exception
            """)
    @WithMockUser
    public void test2() {

        assertThrows(
                AuthorizationDeniedException.class,
                () -> demoController.ciao()
        );
    }

    @Test
    @DisplayName("""
            GIVEN endpoint /ciao is called
            WHEN authentication is provided 
            AND authentication has the correct role
            THEN method call succeeds
            """)
    @WithMockUser(roles = "ADMIN")
    public void test3() {
        String result = demoController.ciao();
        assertEquals("Ciao!", result);
    }
}
