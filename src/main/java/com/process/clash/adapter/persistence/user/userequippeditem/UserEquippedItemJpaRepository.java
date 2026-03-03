package com.process.clash.adapter.persistence.user.userequippeditem;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEquippedItemJpaRepository extends JpaRepository<UserEquippedItemJpaEntity, Long> {

    @Query("""
        select equippedItem
        from UserEquippedItemJpaEntity equippedItem
        where equippedItem.user.id = :userId
          and equippedItem.category = :category
    """)
    Optional<UserEquippedItemJpaEntity> findByUserIdAndCategory(
            @Param("userId") Long userId,
            @Param("category") ProductCategory category
    );

    @Query("""
        select equippedItem
        from UserEquippedItemJpaEntity equippedItem
        where equippedItem.user.id = :userId
    """)
    List<UserEquippedItemJpaEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("""
        select equippedItem
        from UserEquippedItemJpaEntity equippedItem
        where equippedItem.user.id in :userIds
    """)
    List<UserEquippedItemJpaEntity> findAllByUserIdIn(@Param("userIds") Collection<Long> userIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from UserEquippedItemJpaEntity equippedItem
        where equippedItem.user.id = :userId
          and equippedItem.product.id = :productId
    """)
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
