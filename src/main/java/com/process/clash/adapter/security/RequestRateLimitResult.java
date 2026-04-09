package com.process.clash.adapter.security;

public record RequestRateLimitResult(
        boolean consumed,
        long remainingTokens,
        long retryAfterSeconds
) {
}
