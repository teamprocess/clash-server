package com.process.clash.infrastructure.web;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Adapter that can read IP/User-Agent from the current servlet request.
 *
 * NOTE: This component is retained for backwards compatibility but
 * the application should prefer passing AccessContext explicitly.
 */
@Component
public class ServletRequestContextAdapter {

    private ServletRequestAttributes currentAttrs() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) return servletAttrs;
        return null;
    }

    public String getIpAddress() {
        ServletRequestAttributes attrs = currentAttrs();
        if (attrs == null) return null;
        HttpServletRequest req = attrs.getRequest();
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    public String getUserAgent() {
        ServletRequestAttributes attrs = currentAttrs();
        if (attrs == null) return null;
        return attrs.getRequest().getHeader("User-Agent");
    }
}
