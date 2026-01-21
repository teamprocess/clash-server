package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.CheckDuplicateUsernameData;

public interface CheckDuplicatedUsernameUseCase {

    boolean execute(CheckDuplicateUsernameData.Command command);
}
