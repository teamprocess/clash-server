package com.process.clash.infrastructure.config.security;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AllowedOriginService {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "app://clash",
            "https://local.clash.kr:5173"
    );

    public List<String> getAllowedOrigins() {
        return ALLOWED_ORIGINS.stream()
                .filter(origin -> origin != null && !origin.isBlank())
                .map(String::trim)
                .distinct()
                .toList();
    }

    public boolean isAllowed(String origin) {
        if (origin == null || origin.isBlank()) {
            return false;
        }

        return getAllowedOrigins().contains(origin.trim());
    }
}
