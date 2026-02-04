package com.process.clash.adapter.persistence.user.useritem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserItemJpaRepository extends JpaRepository<UserItemJpaEntity, Long> {

    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);
}
