package com.process.clash.adapter.persistence.shop.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseJpaRepository extends JpaRepository<PurchaseJpaEntity, Long> {
}
