package com.process.clash.application.user.user.service;

import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.exception.exception.conflict.EmailAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
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
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final long VERIFICATION_CODE_EXPIRATION_MS = 5 * 60 * 1000L;

	@Override
	@Transactional
	public void execute(SignUpData.Command command) {

		Optional<User> existingUserOptional = userRepositoryPort.findByEmail(command.email());

		User userToSave;

		if (existingUserOptional.isPresent()) {
			// 같은 이메일 회원가입 요청

			User existingUser = existingUserOptional.get();

			// 인증되었고 같은 이메일
			if (existingUser.isActive()) {
				throw new EmailAlreadyExistException();
			}

			// 같은 이메일 다른 아이디 요청 -> 같은 아이디 검사
			if (!existingUser.username().equals(command.username()) && userRepositoryPort.existsByUsername(command.username())) {
				throw new UsernameAlreadyExistException();
			}

			String encoded = passwordEncoder.encode(command.password());
			userToSave = existingUser.updateSignupInfo(command.username(), command.email(), command.name(), encoded);

		} else {
			// 신규 가입

			if (userRepositoryPort.existsByUsername(command.username())) {
				throw new UsernameAlreadyExistException();
			}

			String encoded = passwordEncoder.encode(command.password());
			userToSave = User.createDefault(
					command.username(),
					command.email(),
					command.name(),
					encoded
			);
		}

		userRepositoryPort.save(userToSave);

		// 공통 로직 (인증 코드 저장 및 메일 발송)
		// 이 코드는 위에서 신규 가입이든 덮어쓰기든 상관없이 공통으로 실행됩니다.
		String verificationCode = generateVerificationCode();

		verificationCodePort.saveCode(command.email(), verificationCode, VERIFICATION_CODE_EXPIRATION_MS);

		sendVerificationEmailPort.execute(command.email(), verificationCode);
	}

	private String generateVerificationCode() {
		int code = SECURE_RANDOM.nextInt(1000000);
		return String.format("%06d", code);
	}
}
