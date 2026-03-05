package com.process.clash.adapter.security;

import com.process.clash.application.user.user.data.AuthPrincipal;
import com.process.clash.application.user.user.exception.exception.unauthorized.NotAuthenticatedException;
import com.process.clash.application.user.user.exception.exception.internalserver.ServletContextUnavailableException;
import com.process.clash.application.user.user.exception.exception.unauthorized.UnverifiedEmailException;
import com.process.clash.application.user.user.port.out.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;

    private ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) {
            return servletAttrs;
        }
        return null;
    }

    @Override
    public void createSession(AuthPrincipal principal, boolean rememberMe) {
        UserDetails authUser = userDetailsService.loadUserByUsername(principal.username());

        if (!authUser.isEnabled()) {
            throw new UnverifiedEmailException();
        }

        Authentication token = new UsernamePasswordAuthenticationToken(
            authUser,
            authUser.getPassword(),
            authUser.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        ServletRequestAttributes attrs = currentRequestAttributes();
        if (attrs == null) {
            throw new ServletContextUnavailableException();
        }

        HttpServletRequest req = attrs.getRequest();
        HttpServletResponse res = attrs.getResponse();

        if (res == null) {
            throw new ServletContextUnavailableException();
        }

        if (rememberMe) {
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(req) {
                @Override
                public String getParameter(String name) {
                    if ("remember-me".equals(name)) {
                        return "true";
                    }
                    return super.getParameter(name);
                }
            };

            rememberMeServices.loginSuccess(wrappedRequest, res, token);
        }

        securityContextRepository.saveContext(context, req, res);
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
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        return auth.getName();
    }
}
