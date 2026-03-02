package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.persistence.auth.AuthEventJpaEntity;
import com.process.clash.adapter.persistence.auth.AuthEventJpaRepository;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.application.google.port.out.RecaptchaPort;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

// RANDOM_PORT를 사용하면 실제 서블릿 컨테이너(Tomcat)가 구동됩니다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthEventTest {

    @LocalServerPort
    private int port; // 랜덤으로 할당된 포트 번호

    @Autowired
    private TestRestTemplate restTemplate; // 실제 HTTP 요청을 보낼 클라이언트

    @Autowired
    private AuthEventJpaRepository authEventRepository;

    @Autowired
    private UserRepositoryPort userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String uniqueUsername;

    @MockitoBean
    private VerificationCodePort verificationCodePort;

    @MockitoBean
    private SendVerificationEmailPort sendVerificationEmailPort;

    @MockitoBean
    private SessionManager sessionManager;

    @MockitoBean
    private RecaptchaPort recaptchaPort;

    @BeforeEach
    void setUp() {
        Mockito.when(recaptchaPort.verifyToken(anyString())).thenReturn(true);

        authEventRepository.deleteAll();

        // 테스트 유저 저장
        String encodedPassword = passwordEncoder.encode("password123");
        uniqueUsername = "u" + Long.toString(System.nanoTime(), 36);
        if (uniqueUsername.length() > 20) {
            uniqueUsername = uniqueUsername.substring(0, 20);
        }
        userRepository.save(User.createDefault(uniqueUsername, "test" + System.nanoTime() + "@example.com", "테스터", encodedPassword));
    }

    @Test
    @DisplayName("실제 HTTP 요청을 통해 로그인 시 IP와 기기 정보가 로깅되어야 한다")
    void recordRealHttpEvent() {
        // 1. 요청 준비 (실제 브라우저처럼 User-Agent 설정)
        String url = "http://localhost:" + port + "/api/auth/sign-in";
        String realUserAgent = "test";

        SignInDto.Request body = new SignInDto.Request(uniqueUsername, "password123", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", realUserAgent); // 실제 기기 정보 주입
        headers.set("X-Recaptcha-Token", "test-token");

        HttpEntity<SignInDto.Request> request = new HttpEntity<>(body, headers);

        // 2. 실제 HTTP POST 요청 발송
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 3. 결과 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 4. DB에 어떻게 기록됐는지 확인
        List<AuthEventJpaEntity> logs = authEventRepository.findAll();
        assertThat(logs).isNotEmpty();

        System.out.println("\n================ REAL HTTP LOG RESULTS ================");
        logs.forEach(log -> {
            System.out.printf("이벤트: %s%n", log.getEventType());
            System.out.printf("IP: %s%n", log.getIpAddress());
            System.out.printf("기기: %s%n", log.getDevice());
        });
        System.out.println("=======================================================\n");
    }
}
