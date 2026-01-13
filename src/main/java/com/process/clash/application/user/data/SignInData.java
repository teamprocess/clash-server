package com.process.clash.application.user.data;

import java.io.Serializable;

public class SignInData {

	public record Command(String username, String password) {}

	public record Result(Long id, String username, String name) implements Serializable {}

}
