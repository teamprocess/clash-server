package com.process.clash.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.domain.common.policy.RateLimitPolicy;
import com.process.clash.domain.common.policy.RateLimitRule;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.infrastructure.principle.AuthUser;
import com.process.clash.infrastructure.security.Bucket4jRateLimitAdapter;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends GenericFilterBean {

    private static final String RATE_LIMIT_KEY_PREFIX = "rl:";

    private final LettuceBasedProxyManager<String> bucketProxyManager;
    private final RateLimitPolicy rateLimitPolicy;
    private final Bucket4jRateLimitAdapter adapter;
    private final ObjectMapper objectMapper;

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/api/auth/sign-in",
            "/api/auth/sign-up",
            "/api/auth/signin",
            "/api/auth/signup",
            "/api/auth/username-duplicate-check",
            "/api/auth/verify-email",
            "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html",
            "/actuator/**"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. Auth 경로는 건너뜀
        if (isExcludedPath(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 2. SecurityContext에서 인증된 사용자 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        // 3. User 정보 추출
        AuthUser userDetails = (AuthUser) authentication.getPrincipal();
        Long userId = userDetails.id();
        boolean isAdmin = userDetails.role().equals(Role.ADMIN);

        // 4. 규칙 조회 — null이면 ADMIN → bypass
        RateLimitRule rule = rateLimitPolicy.getRuleFor(isAdmin);
        if (rule == null) {
            chain.doFilter(request, response);
            return;
        }

        // 5. domain 규칙 → bucket4j 설정으로 변환 (Adapter 책임)
        BucketConfiguration configuration = adapter.toBucketConfiguration(rule);

        // 6. Bucket 키로 조회/생성
        String bucketKey = RATE_LIMIT_KEY_PREFIX + userId;
        Bucket bucket = bucketProxyManager.builder()
                .build(bucketKey, configuration);

        // 7. 토큰 소비 시도
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (!probe.isConsumed()) {
            long waitTimeNanos = probe.getNanosToWaitForRefill();

            long waitTimeSeconds = (long) Math.ceil(waitTimeNanos / 1_000_000_000.0);

            httpResponse.setStatus(429);
            httpResponse.setHeader("Retry-After", String.valueOf(waitTimeSeconds));
            httpResponse.setHeader("X-RateLimit-Remaining", "0");
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            // ErrorResponse 생성
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .code("RATE_LIMIT_EXCEEDED")
                    .message("요청 횟수가 너무 많습니다. " + waitTimeSeconds + "초 후 다시 시도해주세요.")
                    .timestamp(LocalDateTime.now())
                    .build();

            // CommonResponse로 래핑
            CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                    .success(false)
                    .error(errorResponse)
                    .status(429)
                    .build();

            // JSON 직렬화
            httpResponse.getWriter().write(objectMapper.writeValueAsString(commonResponse));
            return;
        }

        httpResponse.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));

        // 8. 통과 → 다음 필터/컨트롤러로
        chain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}
