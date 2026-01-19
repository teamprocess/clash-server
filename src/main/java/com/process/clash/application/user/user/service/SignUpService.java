package com.process.clash.application.user.user.service;

import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.domain.user.user.entity.User;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.process.clash.application.user.user.data.SignUpData;
import com.process.clash.application.user.user.port.in.SignUpUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final VerificationCodePort verificationCodePort;
	private final SendVerificationEmailPort sendVerificationEmailPort;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	@Override
	public void execute(SignUpData.Command command) {

		if (userRepositoryPort.existsByUsername(command.username())) {
			throw new UsernameAlreadyExistException();
		}

		String encoded = passwordEncoder.encode(command.password());

		User user = User.createDefault(
				command.username(),
				command.email(),
				command.name(),
				encoded
		);

		userRepositoryPort.save(user);

		String verificationCode = generateVerificationCode();

		verificationCodePort.saveCode(user.email(), verificationCode, 5 * 60 * 1000);

		sendVerificationEmailPort.execute(user.email(), verificationCode);
	}

	private String generateVerificationCode() {
		int code = SECURE_RANDOM.nextInt(1000000);
		return String.format("%06d", code);
	}
}
