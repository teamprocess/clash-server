package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.VerifyEmailData;

public interface VerifyEmailUseCase {

    void execute(VerifyEmailData.Command command);
}
