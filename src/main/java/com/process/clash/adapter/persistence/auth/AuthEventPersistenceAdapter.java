package com.process.clash.adapter.persistence.auth;

import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuthEventPersistenceAdapter implements AuthEventRepositoryPort {

    private final AuthEventJpaRepository repository;

    @Override
    public void recordSignIn(String username, String ipAddress, String device) {
        recordAuthEvent(username, "SIGN_IN", ipAddress, device);
    }

    @Override
    public void recordDevSignIn(String username, String ipAddress, String device) {
        recordAuthEvent(username, "DEV_SIGN_IN", ipAddress, device);
    }

    @Override
    public void recordSignOut(String username, String ipAddress, String device) {
        recordAuthEvent(username, "SIGN_OUT", ipAddress, device);
    }

    @Override
    public void recordSessionExpired(String username, String ipAddress, String device) {
        recordAuthEvent(username, "SESSION_EXPIRED", ipAddress, device);
    }

    private void recordAuthEvent(String username, String eventType, String ipAddress, String device) {
        repository.save(new AuthEventJpaEntity(username, eventType, ipAddress, device, Instant.now()));
    }
}
