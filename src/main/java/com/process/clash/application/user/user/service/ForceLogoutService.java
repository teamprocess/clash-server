package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.port.in.ForceLogoutUseCase;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForceLogoutService implements ForceLogoutUseCase {

    private final SessionManager sessionManager;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(String username) {
        if (!userRepositoryPort.existsByUsername(username)) {
            throw new UserNotFoundException();
        }
        sessionManager.forceLogoutUser(username);
    }
}
