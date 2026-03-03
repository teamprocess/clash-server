package com.process.clash.application.auth.electron.service;

import com.process.clash.application.auth.electron.exception.exception.badrequest.*;
import com.process.clash.application.auth.electron.exception.exception.notfound.UserNotFoundInAuthException;
import com.process.clash.application.auth.electron.port.out.ElectronAuthConfigPort;
import com.process.clash.application.auth.electron.port.out.ElectronAuthStorePort;
import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.data.AuthPrincipal;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElectronAuthService {

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	// 인증 코드 만료 시간: 5분
	private static final long VERIFICATION_CODE_EXPIRATION_MS = 5 * 60 * 1000L;

	// Pending User 캐시 만료 시간: 10분 (인증 코드보다 길게 설정하여 재시도 가능)
	private static final long PENDING_USER_CACHE_TTL_MS = 10 * 60 * 1000L;

	private final ElectronAuthStorePort store;
	private final ElectronAuthConfigPort config;
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final SessionManager sessionManager;
	private final AuthEventRepositoryPort authEventRepositoryPort;
	private final PendingUserCachePort pendingUserCachePort;
	private final VerificationCodePort verificationCodePort;
	private final SendVerificationEmailPort sendVerificationEmailPort;

	public record StartResult(String loginUrl, String state) {}

	public record ExchangeResult(Long userId, String username, String role) {}

	public record StartSignupResult(String signupUrl, String state) {}

	public StartResult start() {
		if (config.getAllowedRedirectUris() == null || config.getAllowedRedirectUris().isEmpty()) {
			throw new IllegalStateException("electron.auth.allowed-redirect-uris is not configured");
		}

		String state = UUID.randomUUID().toString().replace("-", "");
		store.saveState(state);

		String redirectUri = config.getAllowedRedirectUris().get(0);
		String loginUrl = config.getAuthWebUrl()
				+ "?state=" + enc(state)
				+ "&redirectUri=" + enc(redirectUri);

		return new StartResult(loginUrl, state);
	}

	public ExchangeResult exchange(String code, String state, AccessContext accessContext) {
		// 일회성 코드 검증 및 소비
		ElectronAuthStorePort.OneTimeCodePayload payload = store.consumeOneTimeCode(code);
		if (payload == null) {
			throw new InvalidAuthCodeException();
		}
		if (!payload.state().equals(state)) {
			throw new StateMismatchException();
		}

		// user 조회
		User user = userRepositoryPort.findById(payload.userId())
				.orElseThrow(UserNotFoundInAuthException::new);

		// 세션 생성
		AuthPrincipal principal = AuthPrincipal.from(user);
		sessionManager.createSession(principal, true);

		// 로그인 이벤트 기록
		if (payload.noRecaptcha()) {
			authEventRepositoryPort.recordNoRecapchaLogin(user.username(), accessContext.ipAddress(), accessContext.userAgent());
		} else {
			authEventRepositoryPort.recordLogin(user.username(), accessContext.ipAddress(), accessContext.userAgent());
		}

		return new ExchangeResult(user.id(), user.username(), user.role().name());
	}

	// ========== 회원가입 관련 메서드 ==========

	public StartSignupResult startSignup() {
		if (config.getAllowedRedirectUris() == null || config.getAllowedRedirectUris().isEmpty()) {
			throw new IllegalStateException("electron.auth.allowed-redirect-uris is not configured");
		}

		String state = UUID.randomUUID().toString().replace("-", "");
		store.saveState(state);

		String redirectUri = config.getAllowedRedirectUris().get(0);
		String signupUrl = config.getSignupWebUrl()
				+ "?state=" + enc(state)
				+ "&redirectUri=" + enc(redirectUri);

		return new StartSignupResult(signupUrl, state);
	}

	public void signupAndSendEmail(String username, String email, String name, String password, String state, String redirectUri) {
		// redirectUri 검증
		if (!config.getAllowedRedirectUris().contains(redirectUri)) {
			throw new InvalidRedirectUriException();
		}

		// state 검증 (소비하지 않음 - 실패 시 재시도 가능)
		if (!store.validateState(state)) {
			throw new InvalidStateException();
		}

		// recaptcha 검증은 RecaptchaFilter에서 이미 완료됨

		// 유저네임 중복 체크
		if (userRepositoryPort.existsByUsername(username)) {
			throw new UsernameAlreadyExistException();
		}

		// 모든 검증 성공 후 state 소비
		if (!store.consumeState(state)) {
			// 동시 요청으로 이미 소비된 경우
			throw new InvalidStateException();
		}

		try {
			// 사용자 정보 준비 (아직 DB에 저장하지 않음)
			String encodedPassword = passwordEncoder.encode(password);
			User pendingUser = User.createDefault(username, email, name, encodedPassword);

			// 인증 코드 생성
			String verificationCode = generateVerificationCode();

			// Redis에 데이터 저장
			verificationCodePort.saveCode(state, verificationCode, VERIFICATION_CODE_EXPIRATION_MS);
			pendingUserCachePort.save(state, pendingUser, PENDING_USER_CACHE_TTL_MS);
			store.saveSignupSession(state, redirectUri);

			// 이메일 발송 (실패 시 예외 발생)
			sendVerificationEmailPort.execute(email, verificationCode);

		} catch (Exception e) {
			// 이메일 발송 실패 시 보상 로직: Redis 데이터 정리
			verificationCodePort.deleteCode(state);
			pendingUserCachePort.delete(state);
			store.consumeSignupSession(state);
			throw e;
		}
	}

	/**
	 * 이메일 인증 및 리다이렉트
	 *
	 * 주의: state는 회원가입 플로우 전체에서 일관된 식별자로 사용됩니다:
	 * - signupSession: state -> redirectUri 매핑
	 * - pendingUser: state -> 사용자 정보 매핑
	 * - verificationCode: state -> 인증 코드 매핑
	 *
	 * 이렇게 설계한 이유:
	 * 1. 프론트엔드가 state만 유지하면 되어 구현이 단순함
	 * 2. Redis에서 state 기반으로 모든 관련 데이터 조회 가능
	 * 3. state TTL로 모든 임시 데이터가 자동으로 만료됨
	 */
	public String verifyEmailAndRedirect(String verificationCode, String state, String redirectUri) {
		// redirectUri 검증
		if (!config.getAllowedRedirectUris().contains(redirectUri)) {
			throw new InvalidRedirectUriException();
		}

		// 회원가입 세션 조회 및 소비
		String savedRedirectUri = store.consumeSignupSession(state);
		if (savedRedirectUri == null) {
			throw new InvalidStateException();
		}

		// pending user 조회
		User pendingUser = pendingUserCachePort.findByToken(state)
				.orElseThrow(InvalidStateException::new);

		// 인증 코드 검증
		String savedCode = verificationCodePort.getCode(state)
				.orElseThrow(InvalidAuthCodeException::new);
		if (!savedCode.equals(verificationCode)) {
			throw new InvalidAuthCodeException();
		}

		// 인증 코드 삭제
		verificationCodePort.deleteCode(state);

		// 사용자 생성
		User savedUser = userRepositoryPort.save(pendingUser);

		// pending user 삭제
		pendingUserCachePort.delete(state);

		// 일회성 코드 생성
		String code = UUID.randomUUID().toString().replace("-", "");
		store.saveOneTimeCode(code, state, savedUser.id(), false);

		// deep link URL 생성
		return redirectUri
				+ "?code=" + enc(code)
				+ "&state=" + enc(state);
	}

	public boolean checkUsernameDuplicate(String username) {
		return userRepositoryPort.existsByUsername(username);
	}

	private String generateVerificationCode() {
		int code = SECURE_RANDOM.nextInt(1000000);
		return String.format("%06d", code);
	}

	private String enc(String v) {
		return URLEncoder.encode(v, StandardCharsets.UTF_8);
	}
}
