package com.process.clash.infrastructure.config.security;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AllowedOriginService {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "app://clash",
            "https://local.clash.kr:5173"
    );
    private static final Set<String> ALLOWED_ORIGIN_SET = Set.copyOf(ALLOWED_ORIGINS);

    public List<String> getAllowedOrigins() {
        return ALLOWED_ORIGINS;
    }

    public boolean isAllowed(String origin) {
        if (origin == null || origin.isBlank()) {
            return false;
        }

        return ALLOWED_ORIGIN_SET.contains(origin.trim());
    }
}
