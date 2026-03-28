package com.process.clash.application.user.user.service;

import com.process.clash.application.auth.electron.port.out.ElectronAuthConfigPort;
import com.process.clash.application.common.util.TokenGenerator;
import com.process.clash.application.mail.port.out.SendPasswordResetEmailPort;
import com.process.clash.application.user.user.data.ResetPasswordData;
import com.process.clash.application.user.user.port.in.SendPasswordResetEmailUseCase;
import com.process.clash.application.user.user.port.out.PasswordResetTokenPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendPasswordResetEmailService implements SendPasswordResetEmailUseCase {

    private static final long EXPIRATION_MS = 30 * 60 * 1000L; // 30분

    @Value("${app.reset-password.base-url}")
    private String resetBaseUrl;

    private final ElectronAuthConfigPort electronAuthConfigPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordResetTokenPort passwordResetTokenPort;
    private final SendPasswordResetEmailPort sendPasswordResetEmailPort;
    private final TokenGenerator tokenGenerator;

    @Override
    public void execute(ResetPasswordData.SendCommand command) {
        Optional<User> userOpt = userRepositoryPort.findByEmail(command.email());

        // 이메일 존재 여부 노출 금지: 유저가 없어도 동일 응답
        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();
        String token = tokenGenerator.generateCleanToken();
        passwordResetTokenPort.saveToken(token, createTokenPayload(user.id(), command), EXPIRATION_MS);

        String resetLink = resetBaseUrl + token;
        sendPasswordResetEmailPort.execute(user.email(), resetLink);
    }

    private PasswordResetTokenPort.TokenPayload createTokenPayload(
            Long userId,
            ResetPasswordData.SendCommand command
    ) {
        if (!command.hasAuthContext() || !isAllowedRedirectUri(command.redirectUri())) {
            return new PasswordResetTokenPort.TokenPayload(userId, null, null);
        }

        return new PasswordResetTokenPort.TokenPayload(userId, command.state(), command.redirectUri());
    }

    private boolean isAllowedRedirectUri(String redirectUri) {
        return electronAuthConfigPort.getAllowedRedirectUris() != null
                && electronAuthConfigPort.getAllowedRedirectUris().contains(redirectUri);
    }
}
