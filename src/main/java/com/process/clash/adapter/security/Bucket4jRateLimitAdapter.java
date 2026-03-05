package com.process.clash.adapter.security;

import com.process.clash.domain.common.policy.RateLimitRule;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import org.springframework.stereotype.Component;

@Component
public class Bucket4jRateLimitAdapter {

    public BucketConfiguration toBucketConfiguration(RateLimitRule rule) {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(rule.requests())
                .refillGreedy(rule.requests(), rule.period())
                .build();

        return BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();
    }
}
