package com.process.clash.adapter.web.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.application.user.user.port.out.SessionManager;
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.TestConfiguration;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public VerificationCodePort verificationCodePort() {
            return Mockito.mock(VerificationCodePort.class);
        }

        @Bean
        public SendVerificationEmailPort sendVerificationEmailPort() {
            return Mockito.mock(SendVerificationEmailPort.class);
        }

        @Bean
        public SessionManager sessionManager() {
            return Mockito.mock(SessionManager.class);
        }

        @Bean
        public AuthEventRepositoryPort authEventRepositoryPort() {
            return Mockito.mock(AuthEventRepositoryPort.class);
        }
    }

    private void initMockMvc() {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        }
    }

    @Test
    void signup_shouldSucceed() throws Exception {
        initMockMvc();

        String username = "testuser" + System.nanoTime();
        String email = "test" + System.nanoTime() + "@example.com";
        String password = "password123";
        String name = "테스트유저";

        // signup
        SignUpDto.Request signUpRequest = new SignUpDto.Request(username, email, password, name);
        MockHttpServletRequestBuilder signupRequest = post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest));

        mockMvc.perform(signupRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
    }

    @Test
    void signupAndSignin_flow_shouldSucceed() throws Exception {
        initMockMvc();

        String username = "testuser" + System.nanoTime();
        String email = "test" + System.nanoTime() + "@example.com";
        String password = "password123";
        String name = "테스트유저";

        // signup
        SignUpDto.Request signUpRequest = new SignUpDto.Request(username, email, password, name);
        MockHttpServletRequestBuilder signupRequest = post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest));

        mockMvc.perform(signupRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));

        // signin
        SignInDto.Request signInRequest = new SignInDto.Request(username, password, false);
        MockHttpServletRequestBuilder signinRequest = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest));

        mockMvc.perform(signinRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.message").value("로그인을 성공했습니다."));
    }
}
