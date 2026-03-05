package com.process.clash.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.filter.RecaptchaFilter;
import com.process.clash.application.google.port.out.RecaptchaPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecaptchaFilterTest {

    @Mock
    private RecaptchaPort recaptchaPort;

    @Mock
    private FilterChain filterChain;

    private RecaptchaFilter recaptchaFilter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        recaptchaFilter = new RecaptchaFilter(recaptchaPort, objectMapper);
    }

    @Test
    @DisplayName("보호되지 않은 경로는 필터를 통과")
    void doFilter_UnprotectedPath_PassesThrough() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(recaptchaPort, never()).verifyToken(anyString());
    }

    @Test
    @DisplayName("ENVIRONMENT가 dev면 보호 경로도 recaptcha 없이 통과")
    void doFilter_DevEnvironment_PassesThroughWithoutRecaptcha() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-up");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ReflectionTestUtils.setField(recaptchaFilter, "environment", "dev");

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(recaptchaPort, never()).verifyToken(anyString());
    }

    @Test
    @DisplayName("/api/auth/sign-up은 recaptcha 토큰이 필요")
    void doFilter_SignUpPath_RequiresRecaptcha() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-up");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("RECAPTCHA_REQUIRED");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("/api/auth/sign-in은 recaptcha 토큰이 필요")
    void doFilter_SignInPath_RequiresRecaptcha() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("RECAPTCHA_REQUIRED");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("유효한 recaptcha 토큰이 있으면 필터 통과")
    void doFilter_ValidRecaptchaToken_PassesThrough() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-up");
        request.addHeader("X-Recaptcha-Token", "valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(recaptchaPort.verifyToken("valid-token")).thenReturn(true);

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        verify(recaptchaPort).verifyToken("valid-token");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("무효한 recaptcha 토큰이면 403 응답")
    void doFilter_InvalidRecaptchaToken_Returns403() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-up");
        request.addHeader("X-Recaptcha-Token", "invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(recaptchaPort.verifyToken("invalid-token")).thenReturn(false);

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("RECAPTCHA_INVALID");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("빈 recaptcha 토큰이면 403 응답")
    void doFilter_BlankRecaptchaToken_Returns403() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-in");
        request.addHeader("X-Recaptcha-Token", "   ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("RECAPTCHA_REQUIRED");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("/api/auth/signup 경로도 보호됨")
    void doFilter_SignupPath_IsProtected() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/signup");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("/api/auth/signin 경로도 보호됨")
    void doFilter_SigninPath_IsProtected() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/signin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("/api/auth/verify-email 경로도 보호됨")
    void doFilter_VerifyEmailPath_IsProtected() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/verify-email");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("RECAPTCHA_REQUIRED");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("응답 형식이 CommonResponse 구조를 따름")
    void doFilter_ResponseFormat_FollowsCommonResponseStructure() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/sign-up");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        recaptchaFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);

        String responseBody = response.getContentAsString();
        CommonResponse<?> commonResponse = objectMapper.readValue(responseBody, CommonResponse.class);

        assertThat(commonResponse.success()).isFalse();
        assertThat(commonResponse.error()).isNotNull();
        assertThat(commonResponse.error().code()).isEqualTo("RECAPTCHA_REQUIRED");
    }
}
