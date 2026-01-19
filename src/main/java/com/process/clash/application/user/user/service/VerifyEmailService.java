package com.process.clash.application.user.user.service;

import com.process.clash.application.mail.port.out.VerificationCodePort;
import com.process.clash.application.user.user.exception.exception.badrequest.VerificationCodeExpiredOrWrongEmailException;
import com.process.clash.application.user.user.exception.exception.badrequest.VerificationCodeMismatchException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.VerifyEmailUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final VerificationCodePort verificationCodePort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(String email, String code) {
        String savedCode = verificationCodePort.getCode(email)
                .orElseThrow(VerificationCodeExpiredOrWrongEmailException::new);

        if (!savedCode.equals(code)) {
            throw new VerificationCodeMismatchException();
        }

        User user = userRepositoryPort.findByEmail(email).orElseThrow(UserNotFoundException::new);

        User activeUSer= user.active();
        userRepositoryPort.save(activeUSer);

        verificationCodePort.deleteCode(email);
    }
}
