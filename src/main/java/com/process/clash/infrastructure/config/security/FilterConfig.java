package com.process.clash.infrastructure.config.security;

import com.process.clash.adapter.web.filter.RateLimitFilter;
import com.process.clash.adapter.web.filter.RecaptchaFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 필터 이중 등록 방지 설정
 * - @Component로 선언된 필터들이 서블릿 컨테이너에 자동 등록되는 것을 방지
 * - Spring Security 체인에서만 실행되도록 설정
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final RecaptchaFilter recaptchaFilter;
    private final RateLimitFilter rateLimitFilter;

    /**
     * RecaptchaFilter의 서블릿 컨테이너 자동 등록 비활성화
     * Spring Security 체인에서만 실행됨
     */
    @Bean
    public FilterRegistrationBean<RecaptchaFilter> disableRecaptchaAutoRegistration() {
        FilterRegistrationBean<RecaptchaFilter> registration = new FilterRegistrationBean<>(recaptchaFilter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * RateLimitFilter의 서블릿 컨테이너 자동 등록 비활성화
     * Spring Security 체인에서만 실행됨
     */
    @Bean
    public FilterRegistrationBean<RateLimitFilter> disableRateLimitAutoRegistration() {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>(rateLimitFilter);
        registration.setEnabled(false);
        return registration;
    }
}
