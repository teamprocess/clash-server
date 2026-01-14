package com.process.clash.infrastructure.security;

import com.process.clash.application.user.user.data.AuthPrincipal;
import com.process.clash.application.user.user.exception.exception.unauthorized.NotAuthenticatedException;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.infrastructure.principle.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class SpringSecuritySessionAdapter implements SessionManager {

    private final SecurityContextRepository securityContextRepository;
    private final RememberMeServices rememberMeServices;

    private ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) {
            return servletAttrs;
        }
        return null;
    }

    @Override
    public void createSession(AuthPrincipal principal, boolean rememberMe) {
        AuthUser authUser = new AuthUser(principal.id(), principal.username(), "", principal.role());

        Authentication token = new UsernamePasswordAuthenticationToken(
            authUser,
            null,
            authUser.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        ServletRequestAttributes attrs = currentRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No HttpServletRequest available for session creation");
        }

        HttpServletRequest req = attrs.getRequest();
        HttpServletResponse res = attrs.getResponse();
        securityContextRepository.saveContext(context, req, res);

        if (rememberMe) {
            rememberMeServices.loginSuccess(req, res, token);
        }
    }

    @Override
    public void invalidateSession() {
        ServletRequestAttributes attrs = currentRequestAttributes();
        if (attrs != null) {
            HttpServletRequest req = attrs.getRequest();
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            return ud.getUsername();
        }
        return auth.getName();
    }
}
