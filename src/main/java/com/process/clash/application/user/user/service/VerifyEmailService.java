package com.process.clash.application.user.user.service;

import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.data.VerifyEmailData;
import com.process.clash.application.user.user.exception.exception.badrequest.VerificationCodeExpiredOrWrongEmailException;
import com.process.clash.application.user.user.exception.exception.badrequest.VerificationCodeMismatchException;
import com.process.clash.application.user.user.exception.exception.conflict.EmailAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.conflict.UsernameAlreadyExistException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.VerifyEmailUseCase;
import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final VerificationCodePort verificationCodePort;
    private final UserRepositoryPort userRepositoryPort;
    private final PendingUserCachePort pendingUserCachePort;
    private final UserPomodoroSettingRepositoryPort userPomodoroSettingRepositoryPort;

    @Override
    @Transactional
    public void execute(VerifyEmailData.Command command) {

        // 인증 코드 검증
        String savedCode = verificationCodePort.getCode(command.token())
                .orElseThrow(VerificationCodeExpiredOrWrongEmailException::new);

        if (!savedCode.equals(command.code())) {
            throw new VerificationCodeMismatchException();
        }

        User pendingUser = pendingUserCachePort.findByToken(command.token()).orElseThrow(UserNotFoundException::new);

        if (userRepositoryPort.existsByUsername(pendingUser.username())) {
            throw new UsernameAlreadyExistException();
        }

        if (userRepositoryPort.existsByEmail(pendingUser.email())) {
            throw new EmailAlreadyExistException();
        }

        // 인증 완료 처리 후 DB 저장
        // 인증 완료 처리 후 DB에 저장하고, 생성된 ID를 포함한 User 객체를 받습니다.
        User savedUser = userRepositoryPort.save(pendingUser.active());

        // `save`의 반환값을 사용하면 영속성 컨텍스트에 의해 ID가 관리되므로,
        // 명시적인 flush() 호출은 필요하지 않습니다.

        // 뽀모도로 타이머 세팅 추가
        UserPomodoroSetting userPomodoroSetting = UserPomodoroSetting.createDefault(savedUser.id());
        userPomodoroSettingRepositoryPort.save(userPomodoroSetting);

        // Redis 정리
        pendingUserCachePort.delete(command.token());
        verificationCodePort.deleteCode(command.token());
    }
}
