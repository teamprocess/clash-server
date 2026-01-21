package com.process.clash.application.user.user.service;

import com.process.clash.application.common.util.TokenGenerator;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.data.SignUpData;
import com.process.clash.application.user.user.exception.exception.conflict.EmailAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.process.clash.application.user.user.port.in.SignUpUseCase;
import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

	private final TokenGenerator tokenGenerator;
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final VerificationCodePort verificationCodePort;
	private final SendVerificationEmailPort sendVerificationEmailPort;
	private final PendingUserCachePort pendingUserCachePort;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final long VERIFICATION_CODE_EXPIRATION_MS = 5 * 60 * 1000L; // 인증 코드 만료: 5분
	private static final long PENDING_USER_EXPIRATION_MS = 30 * 60 * 1000L; // PENDING USER 만료: 30분(추후 정보 유지 + 코드 재전송 가능 예정)

	@Override
	@Transactional
	public String execute(SignUpData.Command command) {

		if (userRepositoryPort.existsByUsername(command.username())) {
			throw new UsernameAlreadyExistException();
		}

		if (userRepositoryPort.existsByEmail(command.email())) {
			throw new EmailAlreadyExistException();
		}

		String encoded = passwordEncoder.encode(command.password());

		User pendingUser = User.createDefault(
				command.username(),
				command.email(),
				command.name(),
				encoded
		);

		String token = tokenGenerator.generateCleanToken();

		pendingUserCachePort.save(token, pendingUser, PENDING_USER_EXPIRATION_MS);

		String verificationCode = generateVerificationCode();

		verificationCodePort.saveCode(token, verificationCode, VERIFICATION_CODE_EXPIRATION_MS);

		sendVerificationEmailPort.execute(command.email(), verificationCode);

		return token;
	}

	private String generateVerificationCode() {
		int code = SECURE_RANDOM.nextInt(1000000);
		return String.format("%06d", code);
	}
}
