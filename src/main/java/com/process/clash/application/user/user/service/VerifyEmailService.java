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
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final VerificationCodePort verificationCodePort;
    private final UserRepositoryPort userRepositoryPort;
    private final PendingUserCachePort pendingUserCachePort;

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
        userRepositoryPort.save(pendingUser.active());

        // Redis 정리
        pendingUserCachePort.delete(command.token());
        verificationCodePort.deleteCode(command.token());
    }
}
