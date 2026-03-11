package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.exception.exception.badrequest.InvalidPasswordResetTokenException;
import com.process.clash.application.user.user.port.in.ValidatePasswordResetTokenUseCase;
import com.process.clash.application.user.user.port.out.PasswordResetTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ValidatePasswordResetTokenService implements ValidatePasswordResetTokenUseCase {

    private final PasswordResetTokenPort passwordResetTokenPort;

    @Override
    public void execute(String token) {
        passwordResetTokenPort.getUserId(token)
                .orElseThrow(InvalidPasswordResetTokenException::new);
    }
}
