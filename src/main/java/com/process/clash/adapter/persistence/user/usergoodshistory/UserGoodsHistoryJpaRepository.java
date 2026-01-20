package com.process.clash.adapter.persistence.user.usergoodshistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGoodsHistoryJpaRepository extends JpaRepository<UserGoodsHistoryJpaEntity, Long> {
}
