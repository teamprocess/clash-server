package com.process.clash.adapter.web.auth.listener;

import com.process.clash.application.user.port.out.AuthEventRepositoryPort;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionListener implements HttpSessionListener {

    private final AuthEventRepositoryPort authEventRepositoryPort;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // no-op
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Object contextObject = se.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (contextObject instanceof SecurityContext) {
            SecurityContext context = (SecurityContext) contextObject;
            if (context.getAuthentication() != null) {
                String username = context.getAuthentication().getName();
                // session destroyed (expired or invalidated elsewhere)
                authEventRepositoryPort.recordLogout(username, null, null);
            }
        }
    }
}
