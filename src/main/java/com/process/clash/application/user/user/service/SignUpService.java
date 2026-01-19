package com.process.clash.application.user.user.service;

import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.exception.exception.conflict.EmailAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.domain.user.user.entity.User;
import org.springframework.mail.MailSender;
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
		// 1. 아이디 중복 체크 (이건 상태 상관없이 일단 체크)
		if (userRepositoryPort.existsByUsername(command.username())) {
			throw new UsernameAlreadyExistException();
		}

		// 2. 이메일 중복 체크
		Optional<User> existingUser = userRepositoryPort.findByEmail(command.email());

		if (existingUser.isPresent()) {
			User user = existingUser.get();

			// 인증 완료된 계정이라면 예외 발생
			if (user.isActive()) {
				throw new EmailAlreadyExistException();
			}

			// PENDING 상태인 경우
			// 기존 정보를 새 정보로 덮어쓰기 (비밀번호 인코딩 포함)
			String encoded = passwordEncoder.encode(command.password());
			User updatedUser = user.updateSignupInfo(command.username(), command.email(), command.name(), encoded);

			userRepositoryPort.save(updatedUser); // 신규 저장이 아닌 업데이트 발생

		} else {
			// 신규 가입x
			String encoded = passwordEncoder.encode(command.password());
			User user = User.createDefault(
					command.username(),
					command.email(),
					command.name(),
					encoded
			);
			userRepositoryPort.save(user);
		}

		// 3. 기존에 잘 쓰시던 공통 로직 (인증 코드 저장 및 메일 발송)
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
