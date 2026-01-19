package com.process.clash.application.github.exception;

import java.time.Instant;

public class GithubRateLimitException extends RuntimeException {
    private final Instant resetAt;

    public GithubRateLimitException(String message, Instant resetAt) {
        super(message);
        this.resetAt = resetAt;
    }

    public Instant getResetAt() {
        return resetAt;
    }
}
