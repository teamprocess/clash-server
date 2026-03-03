package com.process.clash.application.user.useritem.port.out;

import com.process.clash.domain.user.useritem.entity.UserItem;
import java.util.List;
import java.util.Set;

public interface UserItemRepositoryPort {

    UserItem save(UserItem userItem);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<Long> findProductIdsByUserId(Long userId);

    Set<Long> findOwnedProductIdsByUserIdAndProductIds(Long userId, Set<Long> productIds);
}
