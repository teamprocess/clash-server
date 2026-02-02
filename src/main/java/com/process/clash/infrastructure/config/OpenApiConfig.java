package com.process.clash.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@SecurityScheme(
        name = "CSRF",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-XSRF-TOKEN",
        description = "CSRF 토큰 (쿠키의 XSRF-TOKEN 값을 입력하세요)"
)
@Configuration
public class OpenApiConfig {
}
