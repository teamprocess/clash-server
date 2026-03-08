package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.ResetPasswordData;

public interface SendPasswordResetEmailUseCase {
    void execute(ResetPasswordData.SendCommand command);
}
