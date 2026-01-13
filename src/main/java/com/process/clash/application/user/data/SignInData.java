package com.process.clash.application.user.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SignInData {

	// TODO: 레코드로 변경

	@AllArgsConstructor
	@Getter
	public static class Command {
		private final String username;
		private final String password;
	}

	@AllArgsConstructor
	@Getter
	public static class Result {
		private final Long id;
		private final String username;
		private final String name;
	}

}
