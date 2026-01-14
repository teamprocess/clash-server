package com.process.clash.adapter.persistence.auth;

import com.process.clash.application.user.port.out.AuthEventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthEventPersistenceAdapter implements AuthEventRepositoryPort {

    private final AuthEventJpaRepository repository;

    @Override
    public void recordLogin(String username, String ipAddress, String device) {
        AuthEventJpaEntity entity = new AuthEventJpaEntity(username, "LOGIN", ipAddress, device, LocalDateTime.now());
        repository.save(entity);
    }

    @Override
    public void recordLogout(String username, String ipAddress, String device) {
        AuthEventJpaEntity entity = new AuthEventJpaEntity(username, "LOGOUT", ipAddress, device, LocalDateTime.now());
        repository.save(entity);
    }

    @Override
    public void recordSessionExpired(String username, String ipAddress, String device) {
        AuthEventJpaEntity entity = new AuthEventJpaEntity(username, "SESSION_EXPIRE", ipAddress, device, LocalDateTime.now());
        repository.save(entity);
    }
}
