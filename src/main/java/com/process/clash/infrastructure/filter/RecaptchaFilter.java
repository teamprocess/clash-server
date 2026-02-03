package com.process.clash.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.application.google.port.out.RecaptchaPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecaptchaFilter extends GenericFilterBean {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String RECAPTCHA_HEADER = "X-Recaptcha-Token";

    private final RecaptchaPort recaptchaPort;
    private final ObjectMapper objectMapper;

    private static final Set<String> PROTECTED_PATHS = Set.of(
            "/api/auth/sign-up",
            "/api/auth/sign-in",
            "/api/auth/signup",
            "/api/auth/signin",
            "/api/auth/verify-email",
            "/api/auth/username-duplicate-check",
            "/api/auth/electron/sign-in/start",
            "/api/auth/electron/sign-in",
            "/api/auth/electron/sign-in/exchange",
            "/api/auth/electron/sign-up/start",
            "/api/auth/electron/sign-up",
            "/api/auth/electron/sign-up/username-check",
            "/api/auth/electron/sign-up/verify-email"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // CORS preflight 요청은 건너뛰기
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String path = httpRequest.getRequestURI();

        if (!isProtectedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String recaptchaToken = httpRequest.getHeader(RECAPTCHA_HEADER);

        if (recaptchaToken == null || recaptchaToken.isBlank()) {
            sendRecaptchaRequiredResponse(httpResponse);
            return;
        }

        boolean isValid = recaptchaPort.verifyToken(recaptchaToken);

        if (!isValid) {
            sendRecaptchaInvalidResponse(httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isProtectedPath(String path) {
        return PROTECTED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private void sendErrorResponse(HttpServletResponse response, String errorCode, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .success(false)
                .error(errorResponse)
                .status(HttpServletResponse.SC_FORBIDDEN)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }

    private void sendRecaptchaRequiredResponse(HttpServletResponse response) throws IOException {
        sendErrorResponse(response, "RECAPTCHA_REQUIRED", "Recaptcha 인증이 필요합니다.");
    }

    private void sendRecaptchaInvalidResponse(HttpServletResponse response) throws IOException {
        sendErrorResponse(response, "RECAPTCHA_INVALID", "Recaptcha 인증에 실패했습니다.");
    }
}
