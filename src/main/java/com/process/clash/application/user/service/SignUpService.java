package com.process.clash.application.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.process.clash.application.user.data.SignUpData;
import com.process.clash.application.user.port.in.SignUpUseCase;
import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.model.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void execute(SignUpData.Command command) {
		String encoded = passwordEncoder.encode(command.password());

		User user = new User(
				null,
				null,
				null,
				command.username(),
				command.name(),
				encoded,
				true,
				null,
				false,
				0,
				0,
				null
		);

		userRepositoryPort.save(user);
	}
}
