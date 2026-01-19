package com.process.clash.application.user.user.data;

public class SignUpData {

	public record Command(
			String username, String email, String password, String name
	) {}
}

