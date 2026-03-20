package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import com.process.clash.application.realtime.port.out.SocketTokenPort;
import com.process.clash.infrastructure.config.security.AllowedOriginService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIoAuthService {

    private final SocketTokenPort socketTokenPort;
    private final AllowedOriginService allowedOriginService;

    public boolean isAuthorized(HandshakeData data) {
        return isAllowedOrigin(data) && resolveUserId(data).isPresent();
    }

    public AuthorizationResult authorize(HandshakeData data) {
        return isAuthorized(data)
            ? AuthorizationResult.SUCCESSFUL_AUTHORIZATION
            : AuthorizationResult.FAILED_AUTHORIZATION;
    }

    public Optional<Long> resolveUserId(HandshakeData data) {
        if (data == null) {
            return Optional.empty();
        }
        String token = extractToken(data);
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return socketTokenPort.resolveUserId(token);
    }

    private boolean isAllowedOrigin(HandshakeData data) {
        if (data == null) {
            return false;
        }

        String origin = data.getHttpHeaders().get("Origin");
        boolean allowed = allowedOriginService.isAllowed(origin);

        if (!allowed) {
            log.warn("허용되지 않은 Socket.IO Origin 요청을 거부합니다. origin={}", origin);
        }

        return allowed;
    }

    private String extractToken(HandshakeData data) {
        String authHeader = data.getHttpHeaders().get("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring("Bearer ".length());
        }
        String tokenParam = data.getSingleUrlParam("token");
        if (tokenParam != null && !tokenParam.isBlank()) {
            return tokenParam;
        }
        String cookie = data.getHttpHeaders().get("Cookie");
        if (cookie == null) {
            return null;
        }
        String[] parts = cookie.split(";");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.startsWith("socket_token=")) {
                return trimmed.substring("socket_token=".length());
            }
        }
        return null;
    }
}
