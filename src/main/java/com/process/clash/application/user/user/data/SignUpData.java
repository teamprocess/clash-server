package com.process.clash.application.user.user.data;

import com.process.clash.adapter.web.auth.dto.SignUpDto;

public class SignUpData {

	public record Command(
			String username, String password, String name
	) {
		public static Command fromRequest(SignUpDto.Request request) {
			return new Command(
					request.username(),
					request.password(),
					request.name()
			);
		}
	}
}

