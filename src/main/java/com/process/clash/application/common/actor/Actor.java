package com.process.clash.application.common.actor;

public class Actor {
    private final Long userId;

    public Actor(Long userId) {
        this.userId = userId;
    }

    public Long userId() {
        return userId;
    }
}
