package com.process.clash.application.user.port.in;

import com.process.clash.application.user.data.SignUpData;

public interface SignUpUseCase {

	void execute(SignUpData.Command command);
}
