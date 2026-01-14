package com.process.clash.adapter.web.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private void initMockMvc() {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        }
    }

    @Test
    void signupAndSignin_flow_shouldSucceed() throws Exception {
        initMockMvc();

        String username = "testuser";
        String password = "password123";
        String name = "테스트유저";

        // signup
        SignUpDto.Request signUpRequest = new SignUpDto.Request(username, password, name);
        MockHttpServletRequestBuilder signupRequest = post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest));

        mockMvc.perform(signupRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));

        // signin
        SignInDto.Request signInRequest = new SignInDto.Request(username, password, false);
        MockHttpServletRequestBuilder signinRequest = post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest));

        mockMvc.perform(signinRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.message").value("로그인을 성공했습니다."));
    }
}
