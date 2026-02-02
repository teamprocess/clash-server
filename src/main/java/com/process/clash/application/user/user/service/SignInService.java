package com.process.clash.application.user.user.service;

import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.user.user.data.AuthPrincipal;
import com.process.clash.application.user.user.data.SignInData;
import com.process.clash.application.user.user.exception.exception.unauthorized.InvalidCredentialsException;
import com.process.clash.application.user.user.port.in.SignInUseCase;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService implements SignInUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final SessionManager sessionManager;
	private final AuthEventRepositoryPort authEventRepositoryPort;

	@Override
	public SignInData.Result execute(SignInData.Command command) {
		User user = userRepositoryPort.findByUsername(command.username())
				.orElseThrow(InvalidCredentialsException::new);

		boolean matches = passwordEncoder.matches(command.password(), user.password());
		if (!matches) {
			throw new InvalidCredentialsException();
		}

		AuthPrincipal principal = AuthPrincipal.from(user);
		sessionManager.createSession(principal, command.rememberMe());

		// record login event using explicit access context (guaranteed non-null)
		AccessContext ctx = command.accessContext();
		authEventRepositoryPort.recordLogin(user.username(), ctx.ipAddress(), ctx.userAgent());

		return new SignInData.Result(user.id(), user.username(), user.name(), user.role());
	}
}
