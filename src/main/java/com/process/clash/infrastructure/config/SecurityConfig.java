package com.process.clash.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.process.clash.adapter.web.auth.service.CustomUserDetailsService;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final int TOKEN_VALIDITY_SECONDS =  60 * 60 * 24 * 14;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf
//                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
                // 프로덕선 환경에서는 밑의 코드를 지우고 위 코드를 활성화하세요
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionFixation().changeSessionId() // 로그인 시 세션 ID를 새로 발급
                )
                .rememberMe(remember -> remember
                        .key("UniqueKey")
                        .rememberMeParameter("remember-me")
                        .alwaysRemember(false)
                        .userDetailsService(customUserDetailsService)
                        .tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");

                            // 1. ErrorResponse 생성 (인증 실패용)
                            ErrorResponse errorResponse = ErrorResponse.builder()
                                    .code(CommonStatusCode.UNAUTHORIZED.getCode()) // 실제 사용하는 StatusCode 상수가 있다면 그것을 쓰세요
                                    .message("인증이 필요한 서비스입니다.")
                                    .timestamp(LocalDateTime.now())
                                    .build();

                            // 2. CommonResponse 생성 (부모 객체)
                            CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                                    .success(false)
                                    .error(errorResponse)
                                    .build();

                            // 3. JSON 변환 및 출력
                            String json = objectMapper.writeValueAsString(commonResponse);

                            response.getWriter().write(json);
                        })
                )
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(securityContextRepository())
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public RememberMeServices rememberMeServices(CustomUserDetailsService customUserDetailsService) {
        return new TokenBasedRememberMeServices("UniqueKey", customUserDetailsService);
    }
}

