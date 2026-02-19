package com.process.clash.application.user.user.service;

import com.process.clash.application.common.util.TokenGenerator;
import com.process.clash.application.mail.exception.exception.InvalidMailException;
import com.process.clash.application.mail.exception.exception.MailDeliveryException;
import com.process.clash.application.mail.exception.exception.MailMessageCreationException;
import com.process.clash.application.mail.exception.exception.MailServerAuthenticationException;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.data.SignUpData;
import com.process.clash.application.user.user.exception.exception.conflict.EmailAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.domain.user.user.entity.User;
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

	/**
	 * 회원가입 요청을 처리하고 이메일 인증 세션을 식별하기 위한 토큰을 반환합니다.
         * <p><b>상세 프로세스:</b></p>
	 * <ul>
	 * <li>중복된 사용자 이름 또는 이메일이 있는지 DB에서 확인합니다.</li>
	 * <li>비밀번호를 암호화하여 {@link User} 엔티티를 생성하고 캐시에 임시 보관합니다.</li>
	 * <li>6자리의 난수 인증 코드를 생성하여 저장하고, 해당 메일로 발송합니다.</li>
	 * </ul>
	 *
	 * @param command 회원가입에 필요한 정보를 담은 명령 객체
	 * @return 이메일 인증에 필요한 토큰
	 * @throws UsernameAlreadyExistException 이미 존재하는 username으로 요청한 경우
	 * @throws EmailAlreadyExistException 이미 존재하는 email로 요청한 경우
	 * @throws MailMessageCreationException 메일 메세지 생성 중 오류가 난 경우
	 * @throws MailServerAuthenticationException 메일 서버 인증을 실패한 경우
	 * @throws InvalidMailException 메일 형식이 잘못된 경우
	 * @throws MailDeliveryException 메일 발송 중 서버와 통신 오류가 난 경우
	 *
	 * @author 김연호
	 */
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
