package com.process.clash.adapter.persistence.user.userequippeditem;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEquippedItemJpaRepository extends JpaRepository<UserEquippedItemJpaEntity, Long> {

    Optional<UserEquippedItemJpaEntity> findByUserIdAndCategory(Long userId, ProductCategory category);

    List<UserEquippedItemJpaEntity> findAllByUserId(Long userId);

    List<UserEquippedItemJpaEntity> findAllByUserIdIn(Collection<Long> userIds);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
