package com.process.clash.application.user.user.service;

import com.process.clash.application.auth.electron.port.out.ElectronAuthConfigPort;
import com.process.clash.application.auth.electron.port.out.ElectronAuthStorePort;
import com.process.clash.application.common.util.TokenGenerator;
import com.process.clash.application.user.user.data.ResetPasswordData;
import com.process.clash.application.user.user.exception.exception.badrequest.InvalidPasswordResetTokenException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.ResetPasswordUseCase;
import com.process.clash.application.user.user.port.out.PasswordResetTokenPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import org.springframework.transaction.annotation.Transactional;
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
    private final ElectronAuthConfigPort electronAuthConfigPort;
    private final ElectronAuthStorePort electronAuthStorePort;
    private final TokenGenerator tokenGenerator;

    @Override
    public ResetPasswordData.ResetResult execute(ResetPasswordData.ResetCommand command) {
        PasswordResetTokenPort.TokenPayload tokenPayload = passwordResetTokenPort.getTokenPayload(command.token())
                .orElseThrow(InvalidPasswordResetTokenException::new);
        Long userId = tokenPayload.userId();

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String encodedPassword = passwordEncoder.encode(command.newPassword());
        userRepositoryPort.save(user.withPassword(encodedPassword));

        passwordResetTokenPort.deleteToken(command.token());

        if (tokenPayload.hasAuthContext()) {
            electronAuthStorePort.saveState(tokenPayload.state());
            return new ResetPasswordData.ResetResult(tokenPayload.state(), tokenPayload.redirectUri());
        }

        String state = tokenGenerator.generateCleanToken();
        electronAuthStorePort.saveState(state);
        String redirectUri = getDefaultRedirectUri();

        return new ResetPasswordData.ResetResult(state, redirectUri);
    }

    private String getDefaultRedirectUri() {
        if (electronAuthConfigPort.getAllowedRedirectUris() == null || electronAuthConfigPort.getAllowedRedirectUris().isEmpty()) {
            throw new IllegalStateException("electron.auth.allowed-redirect-uris is not configured");
        }
        return electronAuthConfigPort.getAllowedRedirectUris().get(0);
    }
}
