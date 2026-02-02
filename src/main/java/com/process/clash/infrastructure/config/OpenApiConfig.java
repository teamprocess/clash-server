package com.process.clash.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Clash API")
                        .version("v1")
                        .description("Clash 서버 API 문서"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server()
                        .url("https://api.clash.kr")
                        .description("운영 서버 (HTTPS)"))
                .components(new Components()
                        .addSecuritySchemes("CSRF", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-XSRF-TOKEN")
                                .description("CSRF 토큰 (쿠키의 XSRF-TOKEN 값을 입력하세요)")))
                .addSecurityItem(new SecurityRequirement().addList("CSRF"));
    }
}
