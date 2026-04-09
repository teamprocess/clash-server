package com.process.clash.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.process.clash.adapter.security.RequestRateLimitResult;
import com.process.clash.adapter.security.RequestRateLimiter;
import com.process.clash.adapter.web.common.util.AccessContextResolver;
import com.process.clash.adapter.web.filter.PublicAuthRateLimitFilter;
import com.process.clash.domain.common.policy.RateLimitRule;
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
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicAuthRateLimitFilterTest {

    @Mock
    private RequestRateLimiter requestRateLimiter;

    @Mock
    private AccessContextResolver accessContextResolver;

    @Mock
    private FilterChain filterChain;

    private PublicAuthRateLimitFilter publicAuthRateLimitFilter;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        publicAuthRateLimitFilter = new PublicAuthRateLimitFilter(requestRateLimiter, accessContextResolver, objectMapper);
        ReflectionTestUtils.setField(publicAuthRateLimitFilter, "enabled", true);
        ReflectionTestUtils.setField(publicAuthRateLimitFilter, "requests", 30);
        ReflectionTestUtils.setField(publicAuthRateLimitFilter, "periodSeconds", 60L);
    }

    @Test
    @DisplayName("공개 auth 경로는 IP 기준 버킷으로 레이트리밋을 적용하고 통과 시 remaining 헤더를 남긴다")
    void doFilter_PublicAuthPathConsumesBucketAndPasses() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(accessContextResolver.extractIpAddress(request)).thenReturn("203.0.113.10");
        when(requestRateLimiter.tryConsume(any(), any())).thenReturn(new RequestRateLimitResult(true, 29, 0));

        publicAuthRateLimitFilter.doFilter(request, response, filterChain);

        verify(requestRateLimiter).tryConsume(
                eq("public-auth-rl:web-sign-in:203.0.113.10"),
                eq(new RateLimitRule(30, Duration.ofSeconds(60)))
        );
        assertThat(response.getHeader("X-RateLimit-Remaining")).isEqualTo("29");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("legacy alias 경로도 동일한 공개 auth 버킷을 공유한다")
    void doFilter_LegacyAliasSharesSameBucket() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/signin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(accessContextResolver.extractIpAddress(request)).thenReturn("203.0.113.20");
        when(requestRateLimiter.tryConsume(any(), any())).thenReturn(new RequestRateLimitResult(true, 28, 0));

        publicAuthRateLimitFilter.doFilter(request, response, filterChain);

        verify(requestRateLimiter).tryConsume(
                eq("public-auth-rl:web-sign-in:203.0.113.20"),
                eq(new RateLimitRule(30, Duration.ofSeconds(60)))
        );
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("공개 auth 경로가 초과되면 429와 Retry-After를 반환한다")
    void doFilter_PublicAuthPathReturns429WhenRateLimited() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/password-reset/send");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(accessContextResolver.extractIpAddress(request)).thenReturn("198.51.100.10");
        when(requestRateLimiter.tryConsume(any(), any())).thenReturn(new RequestRateLimitResult(false, 0, 42));

        publicAuthRateLimitFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(429);
        assertThat(response.getHeader("Retry-After")).isEqualTo("42");
        assertThat(response.getContentAsString()).contains("RATE_LIMIT_EXCEEDED");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("필터가 비활성화되면 공개 auth 경로도 그대로 통과한다")
    void doFilter_PassesWhenDisabled() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ReflectionTestUtils.setField(publicAuthRateLimitFilter, "enabled", false);

        publicAuthRateLimitFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(requestRateLimiter, accessContextResolver);
    }

    @Test
    @DisplayName("CORS preflight 요청은 공개 auth 경로라도 레이트리밋을 건너뛴다")
    void doFilter_PassesOptionsRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("OPTIONS");
        request.setRequestURI("/api/auth/electron/sign-in");
        MockHttpServletResponse response = new MockHttpServletResponse();

        publicAuthRateLimitFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(requestRateLimiter, accessContextResolver);
    }

    @Test
    @DisplayName("대상이 아닌 경로는 공개 auth 레이트리밋을 적용하지 않는다")
    void doFilter_PassesNonPublicAuthPath() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/records/today");
        MockHttpServletResponse response = new MockHttpServletResponse();

        publicAuthRateLimitFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(requestRateLimiter, accessContextResolver);
    }
}
