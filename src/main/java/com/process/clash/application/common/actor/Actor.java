package com.process.clash.application.common.actor;

import com.process.clash.domain.common.enums.Role;

public class Actor {
    private final Long id;
    private final Role role;

    public Actor(Long id) {
        this(id, Role.USER);
    }

    public Actor(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public Long id() {
        return id;
    }

    public Role role() {
        return role;
    }

    public boolean isUser() {
        return role == Role.USER;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
