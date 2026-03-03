package com.process.clash.adapter.persistence.user.userequippeditem;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.userequippeditem.port.out.UserEquippedItemRepositoryPort;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.user.userequippeditem.entity.UserEquippedItem;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserEquippedItemPersistenceAdapter implements UserEquippedItemRepositoryPort {

    private final UserEquippedItemJpaRepository userEquippedItemJpaRepository;
    private final UserEquippedItemJpaMapper userEquippedItemJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public UserEquippedItem save(UserEquippedItem userEquippedItem) {
        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userEquippedItem.userId());
        ProductJpaEntity productJpaEntity = productJpaRepository.getReferenceById(userEquippedItem.productId());
        UserEquippedItemJpaEntity savedEntity = userEquippedItemJpaRepository.save(
                userEquippedItemJpaMapper.toJpaEntity(userEquippedItem, userJpaEntity, productJpaEntity)
        );
        return userEquippedItemJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserEquippedItem> findByUserIdAndCategory(Long userId, ProductCategory category) {
        return userEquippedItemJpaRepository.findByUserIdAndCategory(userId, category)
                .map(userEquippedItemJpaMapper::toDomain);
    }

    @Override
    public List<UserEquippedItem> findAllByUserId(Long userId) {
        return userEquippedItemJpaRepository.findAllByUserId(userId).stream()
                .map(userEquippedItemJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserEquippedItem> findAllByUserIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return userEquippedItemJpaRepository.findAllByUserIdIn(userIds).stream()
                .map(userEquippedItemJpaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        userEquippedItemJpaRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
