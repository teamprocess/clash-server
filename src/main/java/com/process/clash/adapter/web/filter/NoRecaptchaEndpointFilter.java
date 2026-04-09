package com.process.clash.adapter.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NoRecaptchaEndpointFilter extends GenericFilterBean {

    private static final String DEV_ENVIRONMENT = "dev";
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Set<String> DEV_ONLY_PATHS = Set.of(
            "/api/auth/no-recaptcha-sign-in",
            "/api/auth/electron/no-recaptcha-sign-in"
    );

    private final ObjectMapper objectMapper;

    @Value("${ENVIRONMENT:prod}")
    private String environment;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())
                || isDevEnvironment()
                || !isDevOnlyPath(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        sendEndpointNotFoundResponse(httpResponse);
    }

    private boolean isDevOnlyPath(String path) {
        return DEV_ONLY_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private boolean isDevEnvironment() {
        return environment != null && DEV_ENVIRONMENT.equalsIgnoreCase(environment.trim());
    }

    private void sendEndpointNotFoundResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .success(false)
                .error(ErrorResponse.of(CommonStatusCode.ENDPOINT_NOT_FOUND))
                .status(HttpServletResponse.SC_NOT_FOUND)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }
}
