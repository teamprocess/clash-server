package com.process.clash.adapter.web.common.util;

import com.process.clash.application.common.data.AccessContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AccessContextResolverImpl implements AccessContextResolver {

    public AccessContext extractAccessContext(HttpServletRequest request) {
        String ip = extractIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        return AccessContext.of(ip, userAgent);
    }

    public String extractIpAddress(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
