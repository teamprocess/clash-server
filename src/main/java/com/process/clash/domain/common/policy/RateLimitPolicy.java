package com.process.clash.domain.common.policy;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimitPolicy {

    public RateLimitRule getRuleFor(boolean isAdmin) {
        if (isAdmin) {
            return null;  // ADMIN은 제한 없음
        }
        return new RateLimitRule(60, Duration.ofMinutes(1));  // USER: 1분당 60회
    }
}