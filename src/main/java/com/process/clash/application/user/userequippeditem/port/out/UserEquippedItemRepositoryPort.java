package com.process.clash.application.user.userequippeditem.port.out;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.user.userequippeditem.entity.UserEquippedItem;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserEquippedItemRepositoryPort {

    UserEquippedItem save(UserEquippedItem userEquippedItem);

    Optional<UserEquippedItem> findByUserIdAndCategory(Long userId, ProductCategory category);

    List<UserEquippedItem> findAllByUserId(Long userId);

    List<UserEquippedItem> findAllByUserIds(Collection<Long> userIds);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
