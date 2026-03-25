package com.process.clash.infrastructure.config.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AllowedOriginServiceTest {

    private final AllowedOriginService service = new AllowedOriginService();

    @Test
    @DisplayName("허용 origin 목록은 코드 상수와 동일해야 한다")
    void getAllowedOrigins_ReturnsStaticOrigins() {
        assertThat(service.getAllowedOrigins()).containsExactly(
                "https://api.clash.kr",
                "app://clash",
                "https://local.clash.kr:5173"
        );
    }

    @Test
    @DisplayName("허용 origin 검사는 trim 후 정확히 일치해야 한다")
    void isAllowed_MatchesTrimmedOrigin() {
        assertThat(service.isAllowed(" app://clash ")).isTrue();
        assertThat(service.isAllowed("app://other")).isFalse();
    }
}
