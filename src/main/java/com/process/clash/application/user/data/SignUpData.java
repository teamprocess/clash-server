package com.process.clash.application.user.data;

public class SignUpData {

	public record Command(
			String username, String password, String name
	) {}
}

