package com.process.clash.application.group.vo;

import com.process.clash.domain.user.user.entity.User;

public record GroupOwnerVo(
    Long id,
    String name
) {
    public static GroupOwnerVo from(User user) {
        return new GroupOwnerVo(user.id(), user.name());
    }
}
