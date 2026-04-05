package com.process.clash.infrastructure.config.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CorsConfigIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Test
    @DisplayName("corsConfigurationSource 빈은 컨텍스트에 등록되고 주입 가능해야 한다")
    void corsConfigurationSourceBean_shouldBeRegisteredAndInjectable() {
        assertThat(applicationContext.containsBean("corsConfigurationSource")).isTrue();

        CorsConfigurationSource bean = applicationContext.getBean(
                "corsConfigurationSource",
                CorsConfigurationSource.class
        );

        assertThat(bean).isSameAs(corsConfigurationSource);
    }

    @Test
    @DisplayName("corsConfigurationSource 빈은 애플리케이션 설정과 동일한 CORS 정책을 제공해야 한다")
    void corsConfigurationSourceBean_shouldExposeConfiguredCorsPolicy() {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/api/auth/sign-in");
        request.addHeader("Origin", "https://api.clash.kr");

        CorsConfiguration configuration = corsConfigurationSource.getCorsConfiguration(request);

        assertThat(configuration).isNotNull();
        assertThat(configuration.getAllowedOrigins()).contains("https://api.clash.kr");
        assertThat(configuration.getAllowedMethods()).contains("POST", "OPTIONS");
        assertThat(configuration.getAllowedHeaders()).contains("*");
        assertThat(configuration.getAllowCredentials()).isTrue();
    }
}
