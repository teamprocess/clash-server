package com.process.clash.adapter.persistence.user.useritem;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.user.useritem.entity.UserItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserItemPersistenceAdapter implements UserItemRepositoryPort {

    private final UserItemJpaRepository userItemJpaRepository;
    private final UserItemJpaMapper userItemJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public UserItem save(UserItem userItem) {
        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userItem.userId());
        ProductJpaEntity productJpaEntity = productJpaRepository.getReferenceById(userItem.productId());
        UserItemJpaEntity savedEntity = userItemJpaRepository.save(
                userItemJpaMapper.toJpaEntity(userItem, userJpaEntity, productJpaEntity)
        );
        return userItemJpaMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        return userItemJpaRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<Long> findProductIdsByUserId(Long userId) {
        return userItemJpaRepository.findProductIdsByUserId(userId);
    }

    @Override
    public Set<Long> findOwnedProductIdsByUserIdAndProductIds(Long userId, Set<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Set.of();
        }
        return userItemJpaRepository.findOwnedProductIdsByUserIdAndProductIds(userId, productIds);
    }
}
