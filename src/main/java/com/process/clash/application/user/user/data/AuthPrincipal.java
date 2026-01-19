package com.process.clash.application.user.user.data;

import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.entity.User;

public record AuthPrincipal(Long id, String username, Role role) {

	public static AuthPrincipal from(User user) {
		return new AuthPrincipal(user.id(), user.username(), user.role());
	}
}
