package com.process.clash.adapter.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.security.RequestRateLimitResult;
import com.process.clash.adapter.security.RequestRateLimiter;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.adapter.web.common.util.AccessContextResolver;
import com.process.clash.domain.common.policy.RateLimitRule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PublicAuthRateLimitFilter extends GenericFilterBean {

    private static final int TOO_MANY_REQUESTS_STATUS = 429;
    private static final String RATE_LIMIT_KEY_PREFIX = "public-auth-rl:";
    private static final Map<String, String> PUBLIC_AUTH_ROUTE_KEYS = Map.ofEntries(
            Map.entry("/api/auth/sign-in", "web-sign-in"),
            Map.entry("/api/auth/signin", "web-sign-in"),
            Map.entry("/api/auth/no-recaptcha-sign-in", "web-sign-in-dev-bypass"),
            Map.entry("/api/auth/sign-up", "web-sign-up"),
            Map.entry("/api/auth/signup", "web-sign-up"),
            Map.entry("/api/auth/verify-email", "web-verify-email"),
            Map.entry("/api/auth/username-duplicate-check", "web-username-check"),
            Map.entry("/api/auth/password-reset/send", "web-password-reset-send"),
            Map.entry("/api/auth/password-reset/validate", "web-password-reset-validate"),
            Map.entry("/api/auth/password-reset/confirm", "web-password-reset-confirm"),
            Map.entry("/api/auth/electron/sign-in/start", "electron-sign-in-start"),
            Map.entry("/api/auth/electron/sign-in", "electron-sign-in"),
            Map.entry("/api/auth/electron/no-recaptcha-sign-in", "electron-sign-in-dev-bypass"),
            Map.entry("/api/auth/electron/sign-in/exchange", "electron-sign-in-exchange"),
            Map.entry("/api/auth/electron/sign-up/start", "electron-sign-up-start"),
            Map.entry("/api/auth/electron/sign-up", "electron-sign-up"),
            Map.entry("/api/auth/electron/sign-up/verify-email", "electron-sign-up-verify-email"),
            Map.entry("/api/auth/electron/sign-up/username-check", "electron-sign-up-username-check")
    );

    private final RequestRateLimiter requestRateLimiter;
    private final AccessContextResolver accessContextResolver;
    private final ObjectMapper objectMapper;

    @Value("${app.auth.public-rate-limit.enabled:true}")
    private boolean enabled;

    @Value("${app.auth.public-rate-limit.requests:30}")
    private int requests;

    @Value("${app.auth.public-rate-limit.period-seconds:60}")
    private long periodSeconds;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod()) || !enabled) {
            chain.doFilter(request, response);
            return;
        }

        String routeKey = PUBLIC_AUTH_ROUTE_KEYS.get(httpRequest.getRequestURI());
        if (routeKey == null) {
            chain.doFilter(request, response);
            return;
        }

        String ipAddress = normalizeIp(accessContextResolver.extractIpAddress(httpRequest));
        RateLimitRule rule = new RateLimitRule(requests, Duration.ofSeconds(periodSeconds));
        String bucketKey = RATE_LIMIT_KEY_PREFIX + routeKey + ":" + ipAddress;

        RequestRateLimitResult result = requestRateLimiter.tryConsume(bucketKey, rule);
        if (!result.consumed()) {
            sendRateLimitExceededResponse(httpResponse, result.retryAfterSeconds());
            return;
        }

        httpResponse.setHeader("X-RateLimit-Remaining", String.valueOf(result.remainingTokens()));
        chain.doFilter(request, response);
    }

    private String normalizeIp(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            return "unknown";
        }
        return ipAddress.trim();
    }

    private void sendRateLimitExceededResponse(HttpServletResponse response, long retryAfterSeconds) throws IOException {
        response.setStatus(TOO_MANY_REQUESTS_STATUS);
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        response.setHeader("X-RateLimit-Remaining", "0");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("RATE_LIMIT_EXCEEDED")
                .message("요청 횟수가 너무 많습니다. " + retryAfterSeconds + "초 후 다시 시도해주세요.")
                .timestamp(LocalDateTime.now())
                .build();

        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .success(false)
                .error(errorResponse)
                .status(TOO_MANY_REQUESTS_STATUS)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }
}
