package com.process.clash.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.infrastructure.service.CustomUserDetailsService;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import com.process.clash.adapter.web.filter.RateLimitFilter;
import com.process.clash.adapter.web.filter.RecaptchaFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

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
    @Order(1)
    public SecurityFilterChain healthSecurityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        http
                .securityMatcher("/health", "/health/**", "/actuator/health", "/actuator/health/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(new RequestAttributeSecurityContextRepository())
                )
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            RememberMeServices rememberMeServices,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
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
                        .requestMatchers(
                                "/api/auth/sign-in",
                                "/api/auth/no-recaptcha-sign-in",
                                "/api/auth/sign-up",
                                "/api/auth/signin",
                                "/api/auth/signup",
                                "/api/auth/username-duplicate-check",
                                "/api/auth/verify-email"
                        ).permitAll()
                        .requestMatchers("/api/auth/password-reset/**").permitAll()
                        .requestMatchers("/api/config/public").permitAll()
                        .requestMatchers("/auth-login.html", "/auth-signup.html").permitAll()
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
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
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public RememberMeServices rememberMeServices(PersistentTokenRepository persistentTokenRepository) {
        PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
                rememberMeKey, customUserDetailsService, persistentTokenRepository) {
            @Override
            protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
                String cookieValue = encodeCookie(tokens);
                String contextPath = request.getContextPath();
                String cookiePath = contextPath.isEmpty() ? "/" : contextPath;

                StringBuilder header = new StringBuilder();
                header.append(getCookieName()).append("=").append(cookieValue);
                header.append("; Path=").append(cookiePath);
                header.append("; Max-Age=").append(maxAge);
                header.append("; HttpOnly");
                header.append("; Secure");
                header.append("; SameSite=None");

                response.addHeader(SET_COOKIE, header.toString());
            }

            @Override
            protected UserDetails processAutoLoginCookie(
                    String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
                if (cookieTokens.length != 2) {
                    throw new InvalidCookieException("Cookie token did not contain 2 tokens, but contained '"
                            + Arrays.asList(cookieTokens) + "'");
                }
                String presentedSeries = cookieTokens[0];
                String presentedToken  = cookieTokens[1];

                PersistentRememberMeToken token = persistentTokenRepository.getTokenForSeries(presentedSeries);
                if (token == null) {
                    throw new RememberMeAuthenticationException(
                            "No persistent token found for series id: " + presentedSeries);
                }

                if (!MessageDigest.isEqual(
                        presentedToken.getBytes(StandardCharsets.UTF_8),
                        token.getTokenValue().getBytes(StandardCharsets.UTF_8))) {
                    persistentTokenRepository.removeUserTokens(token.getUsername());
                    throw new CookieTheftException("Invalid remember-me token (Series/token mismatch)");
                }

                if (token.getDate().getTime() + (long) getTokenValiditySeconds() * 1000 < System.currentTimeMillis()) {
                    throw new RememberMeAuthenticationException("Remember-me login has expired");
                }

                return getUserDetailsService().loadUserByUsername(token.getUsername());
            }

            @Override
            protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
                String contextPath = request.getContextPath();
                String cookiePath = contextPath.isEmpty() ? "/" : contextPath;

                StringBuilder header = new StringBuilder();
                header.append(getCookieName()).append("=");
                header.append("; Path=").append(cookiePath);
                header.append("; Max-Age=0");
                header.append("; HttpOnly");
                header.append("; Secure");
                header.append("; SameSite=None");

                response.addHeader(SET_COOKIE, header.toString());
            }
        };
        services.setParameter("remember-me");
        services.setTokenValiditySeconds(TOKEN_VALIDITY_SECONDS);
        return services;
    }
}
