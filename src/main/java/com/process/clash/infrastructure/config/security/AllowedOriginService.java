package com.process.clash.infrastructure.config.security;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AllowedOriginService {

    private final List<String> allowedOrigins;
    private final Set<String> allowedOriginSet;

    public AllowedOriginService(CorsProperties corsProperties) {
        this.allowedOrigins = corsProperties.allowedOrigins().stream()
                .flatMap(o -> Arrays.stream(o.split(",")))
                .map(String::trim)
                .map(o -> o.endsWith("/") ? o.substring(0, o.length() - 1) : o)
                .filter(o -> !o.isEmpty())
                .toList();
        this.allowedOriginSet = Set.copyOf(this.allowedOrigins);
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public boolean isAllowed(String origin) {
        if (origin == null || origin.isBlank()) {
            return false;
        }

        return allowedOriginSet.contains(origin.trim());
    }
}