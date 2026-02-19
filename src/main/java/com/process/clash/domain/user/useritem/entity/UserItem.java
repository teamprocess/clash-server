package com.process.clash.domain.user.useritem.entity;

import java.time.Instant;

public record UserItem(
        Long id,
        Instant createdAt,
        Long userId,
        Long productId
) {
    public static UserItem create(Long userId, Long productId) {
        return new UserItem(null, null, userId, productId);
    }
}
