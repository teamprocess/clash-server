package com.process.clash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;

@JsonTest
@Import(JacksonConfig.class)
class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializesInstantAsKstOffsetDateTime() throws Exception {
        Sample sample = new Sample(Instant.parse("2026-02-14T00:00:00Z"));

        String json = objectMapper.writeValueAsString(sample);

        assertThat(json).contains("\"occurredAt\":\"2026-02-14T09:00:00+09:00\"");
    }

    private record Sample(Instant occurredAt) {
    }
}
