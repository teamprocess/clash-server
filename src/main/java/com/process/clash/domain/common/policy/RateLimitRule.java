// domain/policy/RateLimitRule.java
package com.process.clash.domain.common.policy;

import java.time.Duration;

public record RateLimitRule(int requests, Duration period) {}