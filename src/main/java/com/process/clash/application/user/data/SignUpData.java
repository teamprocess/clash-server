package com.process.clash.application.user.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SignUpData {

	// TODO
	@AllArgsConstructor
	@Getter
	public static class Command {
		private final String username;
		private final String password;
		private final String name;
	}

	// result can be empty or contain created id; keep minimal
	public static class Result {
	}

}
