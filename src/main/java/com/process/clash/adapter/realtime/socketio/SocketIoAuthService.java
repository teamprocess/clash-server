package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import com.process.clash.application.realtime.port.out.SocketTokenPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketIoAuthService {

    private final SocketTokenPort socketTokenPort;

    public boolean isAuthorized(HandshakeData data) {
        return resolveUserId(data).isPresent();
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
