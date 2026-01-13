package com.process.clash.application.user.data;

public class SignInData {

	public record Command(String username, String password) {}

	public record Result(Long id, String username, String name) {}

}
