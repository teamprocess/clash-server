package com.process.clash.application.user.service;

import com.process.clash.application.user.exception.exception.unauthorized.InvalidCredentialsException;
import com.process.clash.domain.user.user.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.process.clash.application.user.data.SignInData;
import com.process.clash.application.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.port.in.SignInUseCase;
import com.process.clash.application.user.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignInService implements SignInUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;

	@Override
	public SignInData.Result execute(SignInData.Command command) {
		User user = userRepositoryPort.findByUsername(command.username())
				.orElseThrow(UserNotFoundException::new);

		boolean matches = passwordEncoder.matches(command.password(), user.password());
		if (!matches) {
			throw new InvalidCredentialsException();
		}

		return new SignInData.Result(user.id(), user.username(), user.name());
	}
}
