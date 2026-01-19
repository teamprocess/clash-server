package com.process.clash.application.user.user.service;

import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.exception.exception.conflict.EmailAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.domain.user.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.process.clash.application.user.user.data.SignUpData;
import com.process.clash.application.user.user.port.in.SignUpUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final VerificationCodePort verificationCodePort;
	private final SendVerificationEmailPort sendVerificationEmailPort;
	private final PendingUserCachePort pendingUserCachePort;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final long VERIFICATION_CODE_EXPIRATION_MS = 5 * 60 * 1000L;
	private static final long PENDING_USER_EXPIRATION_MS = 30 * 60 * 1000L;

	@Override
	@Transactional
	public void execute(SignUpData.Command command) {

		if (userRepositoryPort.existsByUsername(command.username())) {
			throw new UsernameAlreadyExistException();
		}

		if (userRepositoryPort.existsByUsername(command.username())) {
			throw new UsernameAlreadyExistException();
		}

		String encoded = passwordEncoder.encode(command.password());

		User pendingUser = User.createDefault(
				command.username(),
				command.email(),
				command.name(),
				encoded
		);

		pendingUserCachePort.save(command.email(), pendingUser, PENDING_USER_EXPIRATION_MS);

		String verificationCode = generateVerificationCode();

		verificationCodePort.saveCode(command.email(), verificationCode, VERIFICATION_CODE_EXPIRATION_MS);

		sendVerificationEmailPort.execute(command.email(), verificationCode);

	}

	private String generateVerificationCode() {
		int code = SECURE_RANDOM.nextInt(1000000);
		return String.format("%06d", code);
	}
}
