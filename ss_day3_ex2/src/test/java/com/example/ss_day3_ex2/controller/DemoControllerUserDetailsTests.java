package com.example.ss_day3_ex2.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerUserDetailsTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            GIVEN /demo endpoint is called without authentication
            WHEN no authorization is applied
            THEN the response status should be 200 OK
            """)
    public void test1() throws Exception {
        mockMvc.perform(get("/demo"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            GIVEN /test endpoint is called
            WHEN no authentication is performed
            THEN result is 401 Unauthorized
            """)
    public void test2() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            GIVEN /test endpoint is called
            WHEN with an authenticated user
            THEN result is 200 OK
            """)
    @WithUserDetails
    public void test3() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            GIVEN /hello endpoint is called
            WHEN no authentication is provided
            THEN response status is 401 Unauthorized
            """)
    public void test4() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            GIVEN /hello endpoint is called
            WHEN authentication is provided
            AND authentication doesn't have the right authority
            THEN response status is 403 Forbidden
            """)
    @WithUserDetails
    public void test5() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            GIVEN /hello endpoint is called
            WHEN authentication is provided
            AND authentication has the right authority
            THEN response status is 200 OK
            """)
    @WithUserDetails("bob")
    public void test6() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }
}
