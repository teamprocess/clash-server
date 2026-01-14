package com.process.clash.application.user.user.port.in;

import com.process.clash.application.common.data.AccessContext;

public interface SignOutUseCase {
    void execute(AccessContext accessContext);
}
