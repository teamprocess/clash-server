package com.process.clash.application.user.user.service;

import com.process.clash.application.auth.electron.port.out.ElectronAuthConfigPort;
import com.process.clash.application.auth.electron.port.out.ElectronAuthStorePort;
import com.process.clash.application.common.util.TokenGenerator;
import com.process.clash.application.user.user.data.ResetPasswordData;
import com.process.clash.application.user.user.port.out.PasswordResetTokenPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private PasswordResetTokenPort passwordResetTokenPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ElectronAuthStorePort electronAuthStorePort;

    @Mock
    private ElectronAuthConfigPort electronAuthConfigPort;

    @Mock
    private TokenGenerator tokenGenerator;

    private ResetPasswordService resetPasswordService;

    @BeforeEach
    void setUp() {
        resetPasswordService = new ResetPasswordService(
                passwordResetTokenPort,
                userRepositoryPort,
                passwordEncoder,
                electronAuthConfigPort,
                electronAuthStorePort,
                tokenGenerator
        );
    }

    @Test
    @DisplayName("비밀번호 변경 성공 시 reset token에 저장된 기존 로그인 state를 반환하고 TTL을 갱신한다")
    void execute_returnsStoredLoginState() {
        ResetPasswordData.ResetCommand command = new ResetPasswordData.ResetCommand("reset-token", "newPassword123");
        User user = createUser(1L, "old-password");

        when(passwordResetTokenPort.getTokenPayload("reset-token"))
                .thenReturn(Optional.of(new PasswordResetTokenPort.TokenPayload(1L, "existing-state", "clashapp://auth")));
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encoded-password");

        ResetPasswordData.ResetResult result = resetPasswordService.execute(command);

        assertThat(result.state()).isEqualTo("existing-state");
        assertThat(result.redirectUri()).isEqualTo("clashapp://auth");
        verify(userRepositoryPort).save(any(User.class));
        verify(passwordResetTokenPort).deleteToken("reset-token");
        verify(electronAuthStorePort).saveState("existing-state");
        verify(tokenGenerator, never()).generateCleanToken();
    }

    @Test
    @DisplayName("reset token에 기존 로그인 컨텍스트가 없으면 새 로그인 state를 발급한다")
    void execute_generatesNewLoginStateWhenAuthContextMissing() {
        ResetPasswordData.ResetCommand command = new ResetPasswordData.ResetCommand("reset-token", "newPassword123");
        User user = createUser(1L, "old-password");

        when(passwordResetTokenPort.getTokenPayload("reset-token"))
                .thenReturn(Optional.of(new PasswordResetTokenPort.TokenPayload(1L, null, null)));
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encoded-password");
        when(electronAuthConfigPort.getAllowedRedirectUris()).thenReturn(List.of("clashapp://auth"));
        when(electronAuthConfigPort.getDefaultRedirectUri()).thenReturn("clashapp://auth");
        when(tokenGenerator.generateCleanToken()).thenReturn("fresh-state");

        ResetPasswordData.ResetResult result = resetPasswordService.execute(command);

        assertThat(result.state()).isEqualTo("fresh-state");
        assertThat(result.redirectUri()).isEqualTo("clashapp://auth");
        verify(electronAuthStorePort).saveState("fresh-state");
    }

    private User createUser(Long id, String password) {
        return new User(
                id, Instant.now(), Instant.now(),
                "testuser", "test@example.com", "테스트유저", password,
                Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null,
                false, RankTier.NONE, ExpTier.UNRANKED
        );
    }
}
