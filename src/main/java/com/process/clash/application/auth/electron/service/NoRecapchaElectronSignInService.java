package com.process.clash.application.auth.electron.service;

import com.process.clash.application.auth.electron.exception.exception.badrequest.InvalidRedirectUriException;
import com.process.clash.application.auth.electron.exception.exception.badrequest.InvalidStateException;
import com.process.clash.application.auth.electron.port.in.ElectronLoginUseCase;
import com.process.clash.application.auth.electron.port.out.ElectronAuthConfigPort;
import com.process.clash.application.auth.electron.port.out.ElectronAuthStorePort;
import com.process.clash.application.user.user.exception.exception.unauthorized.InvalidCredentialsException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoRecapchaElectronSignInService implements ElectronLoginUseCase {

	private static final String DUMMY_PASSWORD_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

	private final ElectronAuthStorePort store;
	private final ElectronAuthConfigPort config;
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;

	@Override
	public String execute(String username, String password, String state, String redirectUri) {
		if (!config.getAllowedRedirectUris().contains(redirectUri)) {
			throw new InvalidRedirectUriException();
		}

		if (!store.validateState(state)) {
			throw new InvalidStateException();
		}

		User user = userRepositoryPort.findByUsername(username).orElse(null);

		String passwordToCheck = (user != null) ? user.password() : DUMMY_PASSWORD_HASH;
		boolean matches = passwordEncoder.matches(password, passwordToCheck);

		if (user == null || !matches) {
			throw new InvalidCredentialsException();
		}

		if (!store.consumeState(state)) {
			throw new InvalidStateException();
		}

		String code = UUID.randomUUID().toString().replace("-", "");
		store.saveOneTimeCode(code, state, user.id(), true);

		return redirectUri
				+ "?code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
				+ "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);
	}
}
