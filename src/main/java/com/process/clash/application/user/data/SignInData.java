package com.process.clash.application.user.data;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.Role;

import java.io.Serializable;

public class SignInData {

	public record Command(String username, String password) {}

	public record Result(Long id, String username, String name, Role role) implements Serializable {}

}
