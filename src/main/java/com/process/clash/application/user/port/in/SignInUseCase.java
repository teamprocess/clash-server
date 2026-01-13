package com.process.clash.application.user.port.in;

import com.process.clash.application.user.data.SignInData;

public interface SignInUseCase {

	SignInData.Result execute(SignInData.Command command);
}
