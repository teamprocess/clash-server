package com.process.clash.adapter.web.auth.electron.service;

import com.process.clash.adapter.google.RecaptchaAdapter;
import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.application.auth.electron.exception.exception.badrequest.*;
import com.process.clash.application.auth.electron.exception.exception.notfound.UserNotFoundInAuthException;
import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.data.AuthPrincipal;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.unauthorized.InvalidCredentialsException;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.infrastructure.auth.ElectronAuthStore;
import com.process.clash.infrastructure.config.ElectronAuthProps;
import jakarta.servlet.http.HttpServletRequest;
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

	// 타이밍 공격 방지를 위한 더미 BCrypt 해시
	// 원본: "dummy-password-for-timing-attack-prevention"을 BCrypt로 인코딩한 결과
	private static final String DUMMY_PASSWORD_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	// 인증 코드 만료 시간: 5분
	private static final long VERIFICATION_CODE_EXPIRATION_MS = 5 * 60 * 1000L;
	
	// Pending User 캐시 만료 시간: 10분 (인증 코드보다 길게 설정하여 재시도 가능)
	private static final long PENDING_USER_CACHE_TTL_MS = 10 * 60 * 1000L;

	private final ElectronAuthStore store;
	private final RecaptchaAdapter recaptchaAdapter;
	private final ElectronAuthProps props;
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final SessionManager sessionManager;
	private final AuthEventRepositoryPort authEventRepositoryPort;
	private final PendingUserCachePort pendingUserCachePort;
	private final VerificationCodePort verificationCodePort;
	private final SendVerificationEmailPort sendVerificationEmailPort;

	public ElectronAuthDto.StartResponse start() {
		if (props.getAllowedRedirectUris() == null || props.getAllowedRedirectUris().isEmpty()) {
			throw new IllegalStateException("electron.auth.allowed-redirect-uris is not configured");
		}

		String state = UUID.randomUUID().toString().replace("-", "");
		store.saveState(state);

		String redirectUri = props.getAllowedRedirectUris().get(0);
		String loginUrl = props.getAuthWebUrl()
				+ "?state=" + enc(state)
				+ "&redirectUri=" + enc(redirectUri);

		return new ElectronAuthDto.StartResponse(loginUrl, state);
	}

	public String loginAndRedirect(ElectronAuthDto.LoginRequest req) {
		// redirectUri 검증
		if (!props.getAllowedRedirectUris().contains(req.redirectUri())) {
			throw new InvalidRedirectUriException();
		}

		// state 검증 (소비하지 않음 - 실패 시 재시도 가능)
		if (!store.validateState(req.state())) {
			throw new InvalidStateException();
		}

		// recaptcha 검증
		boolean recaptchaValid = recaptchaAdapter.verifyToken(req.recaptchaToken());
		if (!recaptchaValid) {
			throw new RecaptchaVerificationFailedException();
		}

		// 사용자 인증 (타이밍 공격 방지)
		User user = userRepositoryPort.findByUsername(req.username()).orElse(null);
		
		// 사용자가 존재하지 않아도 더미 비밀번호 비교를 수행하여 실행 시간을 일정하게 유지
		String passwordToCheck = (user != null) ? user.password() : DUMMY_PASSWORD_HASH;
		boolean matches = passwordEncoder.matches(req.password(), passwordToCheck);
		
		if (user == null || !matches) {
			throw new InvalidCredentialsException();
		}

		// 모든 검증 성공 후 state 소비 (이제야 state 사용 완료)
		if (!store.consumeState(req.state())) {
			// 동시 요청으로 이미 소비된 경우
			throw new InvalidStateException();
		}

		// 일회성 코드 생성
		String code = UUID.randomUUID().toString().replace("-", "");
		store.saveOneTimeCode(code, req.state(), user.id());

		// deep link URL 생성
		return req.redirectUri()
				+ "?code=" + enc(code)
				+ "&state=" + enc(req.state());
	}

	public ElectronAuthDto.ExchangeResponse exchange(
			ElectronAuthDto.ExchangeRequest req,
			HttpServletRequest httpRequest
	) {
		// 일회성 코드 검증 및 소비
		ElectronAuthStore.OneTimeCodePayload payload = store.consumeOneTimeCode(req.code());
		if (payload == null) {
			throw new InvalidAuthCodeException();
		}
		if (!payload.state().equals(req.state())) {
			throw new StateMismatchException();
		}

		// user 조회
		User user = userRepositoryPort.findById(payload.userId())
				.orElseThrow(UserNotFoundInAuthException::new);

		// 세션 생성
		AuthPrincipal principal = AuthPrincipal.from(user);
		sessionManager.createSession(principal, true);

		// 로그인 이벤트 기록
		AccessContext ctx = extractAccessContext(httpRequest);
		authEventRepositoryPort.recordLogin(user.username(), ctx.ipAddress(), ctx.userAgent());

		return new ElectronAuthDto.ExchangeResponse(
				user.id(),
				user.username(),
				user.role().name()
		);
	}

	private AccessContext extractAccessContext(HttpServletRequest request) {
		String ip = extractIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		return AccessContext.of(ip, userAgent);
	}

	private String extractIpAddress(HttpServletRequest request) {
		String xff = request.getHeader("X-Forwarded-For");
		if (xff != null && !xff.isEmpty()) {
			return xff.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}

	private String enc(String v) {
		return URLEncoder.encode(v, StandardCharsets.UTF_8);
	}

	// ========== 회원가입 관련 메서드 ==========

	public ElectronAuthDto.StartSignupResponse startSignup() {
		if (props.getAllowedRedirectUris() == null || props.getAllowedRedirectUris().isEmpty()) {
			throw new IllegalStateException("electron.auth.allowed-redirect-uris is not configured");
		}

		String state = UUID.randomUUID().toString().replace("-", "");
		store.saveState(state);

		String redirectUri = props.getAllowedRedirectUris().get(0);
		String signupUrl = props.getSignupWebUrl()
				+ "?state=" + enc(state)
				+ "&redirectUri=" + enc(redirectUri);

		return new ElectronAuthDto.StartSignupResponse(signupUrl, state);
	}

	public void signupAndSendEmail(ElectronAuthDto.SignupRequest req) {
		// redirectUri 검증
		if (!props.getAllowedRedirectUris().contains(req.redirectUri())) {
			throw new InvalidRedirectUriException();
		}

		// state 검증 (소비하지 않음 - 실패 시 재시도 가능)
		if (!store.validateState(req.state())) {
			throw new InvalidStateException();
		}

		// recaptcha 검증
		boolean recaptchaValid = recaptchaAdapter.verifyToken(req.recaptchaToken());
		if (!recaptchaValid) {
			throw new RecaptchaVerificationFailedException();
		}

		// 유저네임 중복 체크
		if (userRepositoryPort.existsByUsername(req.username())) {
			throw new UsernameAlreadyExistException();
		}

		// 모든 검증 성공 후 state 소비
		if (!store.consumeState(req.state())) {
			// 동시 요청으로 이미 소비된 경우
			throw new InvalidStateException();
		}

		try {
			// 사용자 정보 준비 (아직 DB에 저장하지 않음)
			String encodedPassword = passwordEncoder.encode(req.password());
			User pendingUser = User.createDefault(req.username(), req.email(), req.username(), encodedPassword);

			// 인증 코드 생성
			String verificationCode = generateVerificationCode();
			
			// Redis에 데이터 저장
			verificationCodePort.saveCode(req.state(), verificationCode, VERIFICATION_CODE_EXPIRATION_MS);
			pendingUserCachePort.save(req.state(), pendingUser, PENDING_USER_CACHE_TTL_MS);
			store.saveSignupSession(req.state(), req.redirectUri());
			
			// 이메일 발송 (실패 시 예외 발생)
			sendVerificationEmailPort.execute(req.email(), verificationCode);
			
		} catch (Exception e) {
			// 이메일 발송 실패 시 보상 로직: Redis 데이터 정리
			verificationCodePort.deleteCode(req.state());
			pendingUserCachePort.delete(req.state());
			store.consumeSignupSession(req.state());
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
	public String verifyEmailAndRedirect(ElectronAuthDto.VerifyEmailRequest req) {
		// redirectUri 검증
		if (!props.getAllowedRedirectUris().contains(req.redirectUri())) {
			throw new InvalidRedirectUriException();
		}

		// 회원가입 세션 조회 및 소비
		String redirectUri = store.consumeSignupSession(req.state());
		if (redirectUri == null) {
			throw new InvalidStateException();
		}

		// pending user 조회
		User pendingUser = pendingUserCachePort.findByToken(req.state())
				.orElseThrow(InvalidStateException::new);

		// 인증 코드 검증
		String savedCode = verificationCodePort.getCode(req.state())
				.orElseThrow(InvalidAuthCodeException::new);
		if (!savedCode.equals(req.verificationCode())) {
			throw new InvalidAuthCodeException();
		}

		// 인증 코드 삭제
		verificationCodePort.deleteCode(req.state());

		// 사용자 생성
		User savedUser = userRepositoryPort.save(pendingUser);

		// pending user 삭제
		pendingUserCachePort.delete(req.state());

		// 일회성 코드 생성
		String code = UUID.randomUUID().toString().replace("-", "");
		store.saveOneTimeCode(code, req.state(), savedUser.id());

		// deep link URL 생성
		return req.redirectUri()
				+ "?code=" + enc(code)
				+ "&state=" + enc(req.state());
	}

	public boolean checkUsernameDuplicate(String username) {
		return userRepositoryPort.existsByUsername(username);
	}

	private String generateVerificationCode() {
		int code = SECURE_RANDOM.nextInt(1000000);
		return String.format("%06d", code);
	}

	// exchangeSignup은 기존 exchange와 동일하므로 재사용
}
