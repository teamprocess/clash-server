package com.process.clash.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.auth.service.CustomUserDetailsService;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import com.process.clash.infrastructure.filter.RateLimitFilter;
import com.process.clash.infrastructure.filter.RecaptchaFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RateLimitFilter rateLimitFilter;
    private final RecaptchaFilter recaptchaFilter;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;
    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionFixation().changeSessionId() // 로그인 시 세션 ID를 새로 발급
                )
                .rememberMe(remember -> remember
                        .key(rememberMeKey)
                        .rememberMeParameter("remember-me")
                        .alwaysRemember(false)
                        .userDetailsService(customUserDetailsService)
                        .tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
                        .rememberMeServices(rememberMeServices)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/electron/**").permitAll()
                        .requestMatchers("/api/auth/sign-in", "/api/auth/sign-up", "/api/auth/signin", "/api/auth/signup", "/api/auth/username-duplicate-check", "/api/auth/verify-email").permitAll()
                        .requestMatchers("/api/config/public").permitAll()
                        .requestMatchers("/auth-login.html", "/auth-signup.html", "/admin/category-image-upload.html").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/groups/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 1. HTTP 응답 헤더 설정
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");

                            // 2. 일관된 에러 객체 생성 (팩토리 메서드 활용)
                            // ErrorResponse.of 내부에서 timestamp와 code, message를 자동으로 채워줍니다.
                            ErrorResponse errorResponse = ErrorResponse.of(CommonStatusCode.UNAUTHORIZED);

                            // 3. 공통 응답 포맷으로 감싸기
                            CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                                    .success(false)
                                    .error(errorResponse)
                                    .build();

                            // 4. JSON 변환 및 출력
                            response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
                        })
                )
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(securityContextRepository())
                )
                .addFilterBefore(recaptchaFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter, SecurityContextHolderFilter.class);

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
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(rememberMeKey, customUserDetailsService) {
            @Override
            protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
                super.setCookie(tokens, maxAge, request, response);

                String cookieHeader = response.getHeader(org.springframework.http.HttpHeaders.SET_COOKIE);
                if (cookieHeader != null && cookieHeader.contains("remember-me")) {
                    // SameSite=None과 Secure를 추가
                    response.setHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookieHeader + "; SameSite=None; Secure");
                }
            }
        };

        services.setParameter("remember-me");
        services.setTokenValiditySeconds(TOKEN_VALIDITY_SECONDS);
        return services;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 사실상 "모든 도메인" 허용 (CORS 무력화)
        configuration.addAllowedOriginPattern("*");

        // 모든 메서드(GET, POST...) 허용
        configuration.addAllowedMethod("*");

        // 모든 헤더 허용
        configuration.addAllowedHeader("*");

        // 쿠키/세션 허용 (인증 사용 중이므로 필수)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
