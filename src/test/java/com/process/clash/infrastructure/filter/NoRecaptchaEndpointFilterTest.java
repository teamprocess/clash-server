package com.process.clash.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.process.clash.adapter.web.filter.NoRecaptchaEndpointFilter;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoRecaptchaEndpointFilterTest {

    @Mock
    private FilterChain filterChain;

    private NoRecaptchaEndpointFilter noRecaptchaEndpointFilter;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        noRecaptchaEndpointFilter = new NoRecaptchaEndpointFilter(objectMapper);
        ReflectionTestUtils.setField(noRecaptchaEndpointFilter, "environment", "prod");
    }

    @Test
    @DisplayName("운영 환경에서는 웹 no-recaptcha 로그인 엔드포인트를 404로 차단")
    void doFilter_BlocksWebNoRecaptchaEndpointInNonDev() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/no-recaptcha-sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        noRecaptchaEndpointFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).contains("ENDPOINT_NOT_FOUND");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("운영 환경에서는 Electron no-recaptcha 로그인 엔드포인트를 404로 차단")
    void doFilter_BlocksElectronNoRecaptchaEndpointInNonDev() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/electron/no-recaptcha-sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        noRecaptchaEndpointFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).contains("ENDPOINT_NOT_FOUND");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("개발 환경에서는 no-recaptcha 로그인 엔드포인트를 통과")
    void doFilter_AllowsNoRecaptchaEndpointInDev() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/no-recaptcha-sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ReflectionTestUtils.setField(noRecaptchaEndpointFilter, "environment", "dev");

        noRecaptchaEndpointFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("CORS preflight 요청은 no-recaptcha 엔드포인트라도 통과")
    void doFilter_AllowsOptionsRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("OPTIONS");
        request.setRequestURI("/api/auth/no-recaptcha-sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        noRecaptchaEndpointFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("대상이 아닌 경로는 그대로 통과")
    void doFilter_AllowsNonProtectedPath() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        noRecaptchaEndpointFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
