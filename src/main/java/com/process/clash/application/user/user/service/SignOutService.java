package com.process.clash.application.user.user.service;

import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.user.user.port.in.SignOutUseCase;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.application.user.user.port.out.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignOutService implements SignOutUseCase {

    private final SessionManager sessionManager;
    private final AuthEventRepositoryPort authEventRepositoryPort;

    @Override
    public void execute(AccessContext accessContext) {
        // AccessContext is required and should be validated by DTO/Command construction

        // If not authenticated, getCurrentUsername() will throw NotAuthenticatedException
        String username = sessionManager.getCurrentUsername();

        // invalidate session
        sessionManager.invalidateSession();

        authEventRepositoryPort.recordLogout(username, accessContext.ipAddress(), accessContext.userAgent());
    }
}
