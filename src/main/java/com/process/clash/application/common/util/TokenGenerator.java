package com.process.clash.application.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGenerator {

    public String generateCleanToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
