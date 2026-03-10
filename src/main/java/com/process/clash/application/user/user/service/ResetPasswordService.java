package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.data.ResetPasswordData;
import com.process.clash.application.user.user.exception.exception.badrequest.InvalidPasswordResetTokenException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.ResetPasswordUseCase;
import com.process.clash.application.user.user.port.out.PasswordResetTokenPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCase {

    private final PasswordResetTokenPort passwordResetTokenPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute(ResetPasswordData.ResetCommand command) {
        Long userId = passwordResetTokenPort.getUserId(command.token())
                .orElseThrow(InvalidPasswordResetTokenException::new);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String encodedPassword = passwordEncoder.encode(command.newPassword());
        userRepositoryPort.save(user.withPassword(encodedPassword));

        passwordResetTokenPort.deleteToken(command.token());
    }
}
