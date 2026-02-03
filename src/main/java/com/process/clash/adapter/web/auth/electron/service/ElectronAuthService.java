package com.process.clash.adapter.web.auth.electron.service;

import com.process.clash.adapter.google.RecaptchaAdapter;
import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.application.auth.electron.exception.exception.badrequest.*;
import com.process.clash.application.auth.electron.exception.exception.notfound.UserNotFoundInAuthException;
import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.user.user.data.AuthPrincipal;
import com.process.clash.application.user.user.exception.exception.unauthorized.InvalidCredentialsException;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElectronAuthService {

	// 타이밍 공격 방지를 위한 더미 BCrypt 해시
	// 원본: "dummy-password-for-timing-attack-prevention"을 BCrypt로 인코딩한 결과
	private static final String DUMMY_PASSWORD_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

	private final ElectronAuthStore store;
	private final RecaptchaAdapter recaptchaAdapter;
	private final ElectronAuthProps props;
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final SessionManager sessionManager;
	private final AuthEventRepositoryPort authEventRepositoryPort;

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

		// state 검증 및 소비
		if (!store.consumeState(req.state())) {
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
}
