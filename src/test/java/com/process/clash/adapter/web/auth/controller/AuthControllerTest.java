package com.process.clash.adapter.web.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.application.user.user.data.SignInData;
import com.process.clash.application.user.user.port.in.SignInUseCase;
import com.process.clash.application.user.user.port.in.SignUpUseCase;
import com.process.clash.domain.user.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @MockitoBean
    private SignUpUseCase signUpUseCase;

    @MockitoBean
    private SignInUseCase signInUseCase;

    private void initMockMvc() {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        }
    }

    @Test
    void signup_shouldSucceed() throws Exception {
        initMockMvc();
        when(signUpUseCase.execute(any())).thenReturn("signup_token");

        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        String name = "테스트유저";

        // signup
        SignUpDto.Request signUpRequest = new SignUpDto.Request(username, email, password, name);
        mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입 요청 / 이메일 인증 코드 발송이 완료되었습니다."));
    }

    @Test
    void signupAndSignin_flow_shouldSucceed() throws Exception {
        initMockMvc();

        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        String name = "테스트유저";

        when(signUpUseCase.execute(any())).thenReturn("signup_token");
        when(signInUseCase.execute(any())).thenReturn(new SignInData.Result(1L, username, name, Role.USER));

        // signup
        SignUpDto.Request signUpRequest = new SignUpDto.Request(username, email, password, name);
        mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입 요청 / 이메일 인증 코드 발송이 완료되었습니다."));

        // signin
        SignInDto.Request signInRequest = new SignInDto.Request(username, password, false);
        mockMvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.message").value("로그인을 성공했습니다."));
    }
}
