package com.process.clash.adapter.persistence.user.usergoodshistory;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGoodsHistoryJpaRepository extends JpaRepository<UserGoodsHistoryJpaEntity, Long> {

    @Query("""
        select distinct u.product.id
        from UserGoodsHistoryJpaEntity u
        where u.user.id = :userId
          and u.product is not null
    """)
    List<Long> findDistinctProductIdsByUserId(@Param("userId") Long userId);
}
