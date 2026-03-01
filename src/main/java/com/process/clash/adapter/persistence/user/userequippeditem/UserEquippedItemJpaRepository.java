package com.process.clash.adapter.persistence.user.userequippeditem;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEquippedItemJpaRepository extends JpaRepository<UserEquippedItemJpaEntity, Long> {

    Optional<UserEquippedItemJpaEntity> findByUser_IdAndCategory(Long userId, ProductCategory category);

    List<UserEquippedItemJpaEntity> findAllByUser_Id(Long userId);

    List<UserEquippedItemJpaEntity> findAllByUser_IdIn(Collection<Long> userIds);

    void deleteByUser_IdAndProduct_Id(Long userId, Long productId);
}
