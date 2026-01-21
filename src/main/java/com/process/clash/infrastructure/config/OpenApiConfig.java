package com.process.clash.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Clash API",
                version = "v1",
                description = "Clash 서버 API 문서"
        ),
        servers = {
                @Server(url = "https://api.clash.kr", description = "운영 서버 (HTTPS)"),
        }
)
@Configuration
public class OpenApiConfig {
}
