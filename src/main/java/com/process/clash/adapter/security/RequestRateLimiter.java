package com.process.clash.adapter.security;

import com.process.clash.domain.common.policy.RateLimitRule;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestRateLimiter {

    private final LettuceBasedProxyManager<String> bucketProxyManager;
    private final Bucket4jRateLimitAdapter bucket4jRateLimitAdapter;

    public RequestRateLimitResult tryConsume(String bucketKey, RateLimitRule rule) {
        BucketConfiguration configuration = bucket4jRateLimitAdapter.toBucketConfiguration(rule);
        Bucket bucket = bucketProxyManager.builder().build(bucketKey, () -> configuration);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            return new RequestRateLimitResult(true, probe.getRemainingTokens(), 0);
        }

        long retryAfterSeconds = (long) Math.ceil(probe.getNanosToWaitForRefill() / 1_000_000_000.0);
        return new RequestRateLimitResult(false, 0, retryAfterSeconds);
    }
}
