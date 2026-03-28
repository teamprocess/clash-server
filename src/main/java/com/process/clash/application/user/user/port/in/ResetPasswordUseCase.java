package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.ResetPasswordData;

public interface ResetPasswordUseCase {
    ResetPasswordData.ResetResult execute(ResetPasswordData.ResetCommand command);
}
