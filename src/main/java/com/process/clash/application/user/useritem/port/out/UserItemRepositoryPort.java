package com.process.clash.application.user.useritem.port.out;

import com.process.clash.domain.user.useritem.entity.UserItem;

public interface UserItemRepositoryPort {

    UserItem save(UserItem userItem);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
