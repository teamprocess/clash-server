package com.process.clash.adapter.persistence.user.useritem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserItemJpaRepository extends JpaRepository<UserItemJpaEntity, Long> {

    @Query("""
        select count(ui) > 0
        from UserItemJpaEntity ui
        where ui.user.id = :userId
          and ui.product.id = :productId
    """)
    boolean existsByUserIdAndProductId(
            @Param("userId") Long userId,
            @Param("productId") Long productId
    );
}
