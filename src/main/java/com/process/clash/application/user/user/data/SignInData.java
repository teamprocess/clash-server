package com.process.clash.application.user.user.data;

import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.user.user.exception.exception.internalserver.AccessContextMissingException;
import com.process.clash.domain.user.user.enums.Role;

import java.io.Serializable;

public class SignInData {

	public record Command(String username, String password, boolean rememberMe, AccessContext accessContext) {
		public Command {
			if (accessContext == null) {
				throw new AccessContextMissingException();
			}
		}
	}

	public record Result(Long id, String username, String name, Role role) implements Serializable {}

}
