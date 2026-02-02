package com.process.clash.domain.common.policy;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimitPolicy {

    private static final int USER_REQUESTS_PER_MINUTE = 100;
    private static final Duration USER_RATE_LIMIT_PERIOD = Duration.ofMinutes(1);

    public RateLimitRule getRuleFor(boolean isAdmin) {
        if (isAdmin) {
            return null;  // ADMIN은 제한 없음
        }
        return new RateLimitRule(USER_REQUESTS_PER_MINUTE, USER_RATE_LIMIT_PERIOD);  // USER: 1분당 60회
    }
}