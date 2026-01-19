package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.data.VerifyEmailData;
import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.exception.exception.badrequest.VerificationCodeExpiredOrWrongEmailException;
import com.process.clash.application.user.user.exception.exception.badrequest.VerificationCodeMismatchException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.VerifyEmailUseCase;
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

    @Override
    @Transactional
    public void execute(VerifyEmailData.Command command) {
        String savedCode = verificationCodePort.getCode(command.email())
                .orElseThrow(VerificationCodeExpiredOrWrongEmailException::new);

        if (!savedCode.equals(command.code())) {
            throw new VerificationCodeMismatchException();
        }

        User user = userRepositoryPort.findByEmail(command.email()).orElseThrow(UserNotFoundException::new);

        User activeUser = user.active();
        userRepositoryPort.save(activeUSer);

        verificationCodePort.deleteCode(command.email());
    }
}
