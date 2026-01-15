package com.process.clash.adapter.web.auth.listener;

import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements HttpSessionListener {

    private final AuthEventRepositoryPort authEventRepositoryPort;

    public SessionListener(@Lazy AuthEventRepositoryPort authEventRepositoryPort) {
        this.authEventRepositoryPort = authEventRepositoryPort;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 아무 작업도 수행하지 않음
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Object contextObject = se.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (contextObject instanceof SecurityContext) {
            SecurityContext context = (SecurityContext) contextObject;
            if (context.getAuthentication() != null) {
                String username = context.getAuthentication().getName();
                // 세션 종료됨 (만료되었거나 다른 곳에서 무효화됨)
                authEventRepositoryPort.recordSessionExpired(username, null, null);
            }
        }
    }
}
