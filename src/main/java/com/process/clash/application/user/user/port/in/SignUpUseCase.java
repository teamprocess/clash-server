package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.SignUpData;

public interface SignUpUseCase {

	String execute(SignUpData.Command command);
}
