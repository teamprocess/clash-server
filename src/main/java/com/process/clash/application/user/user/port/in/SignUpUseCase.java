package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.SignUpData;

public interface SignUpUseCase {

	void execute(SignUpData.Command command);
}
