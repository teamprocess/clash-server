package com.process.clash.adapter.persistence.user.useritem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface UserItemJpaRepository extends JpaRepository<UserItemJpaEntity, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Query("""
            SELECT ui.product.id
            FROM UserItemJpaEntity ui
            WHERE ui.user.id = :userId
              AND ui.product.id IN :productIds
            """)
    Set<Long> findOwnedProductIdsByUserIdAndProductIds(
            @Param("userId") Long userId,
            @Param("productIds") Set<Long> productIds
    );
}
