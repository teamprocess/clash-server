package com.process.clash.application.auth.electron.service;

import com.process.clash.application.auth.electron.port.out.ElectronAuthConfigPort;
import com.process.clash.application.auth.electron.port.out.ElectronAuthStorePort;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElectronAuthServiceTest {

	@Mock
	private ElectronAuthStorePort store;

	@Mock
	private ElectronAuthConfigPort config;

	@Mock
	private UserRepositoryPort userRepositoryPort;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private SessionManager sessionManager;

	@Mock
	private AuthEventRepositoryPort authEventRepositoryPort;

	@Mock
	private PendingUserCachePort pendingUserCachePort;

	@Mock
	private VerificationCodePort verificationCodePort;

	@Mock
	private SendVerificationEmailPort sendVerificationEmailPort;

	private ElectronAuthService electronAuthService;

	@BeforeEach
	void setUp() {
		electronAuthService = new ElectronAuthService(
				store,
				config,
				userRepositoryPort,
				passwordEncoder,
				sessionManager,
				authEventRepositoryPort,
				pendingUserCachePort,
				verificationCodePort,
				sendVerificationEmailPort
		);
	}

	@Test
	@DisplayName("Electron 로그인 시작은 dev 채널에서 dev redirect uri를 사용한다")
	void start_usesDevRedirectUriForDevChannel() {
		when(config.getAllowedRedirectUris()).thenReturn(List.of("clashapp://auth", "clashapp-dev://auth"));
		when(config.getDevRedirectUri()).thenReturn("clashapp-dev://auth");
		when(config.getAuthWebUrl()).thenReturn("https://clash.kr/#/sign-in");

		ElectronAuthService.StartResult result = electronAuthService.start("dev");

		assertThat(result.loginUrl()).contains("redirectUri=clashapp-dev%3A%2F%2Fauth");
		verify(store).saveState(result.state());
	}

	@Test
	@DisplayName("Electron 회원가입 시작은 채널이 없으면 기본 redirect uri를 사용한다")
	void startSignup_usesDefaultRedirectUriWhenChannelMissing() {
		when(config.getAllowedRedirectUris()).thenReturn(List.of("clashapp://auth", "clashapp-dev://auth"));
		when(config.getDefaultRedirectUri()).thenReturn("clashapp://auth");
		when(config.getSignupWebUrl()).thenReturn("https://clash.kr/#/sign-up");

		ElectronAuthService.StartSignupResult result = electronAuthService.startSignup(null);

		assertThat(result.signupUrl()).contains("redirectUri=clashapp%3A%2F%2Fauth");
		verify(store).saveState(result.state());
	}
}
