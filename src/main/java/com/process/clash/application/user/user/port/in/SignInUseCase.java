package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.SignInData;

public interface SignInUseCase {

	SignInData.Result execute(SignInData.Command command);
}
