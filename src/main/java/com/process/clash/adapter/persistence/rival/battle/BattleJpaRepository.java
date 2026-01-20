package com.process.clash.adapter.persistence.rival.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleJpaRepository extends JpaRepository<BattleJpaEntity, Long> {

    @Query("""
        select case when count(b) > 0 then true else false end
        from BattleJpaEntity b
        where b.battleStatus <> 'DONE'
          and (b.rival.firstUser.id = :userId
            or b.rival.secondUser.id = :userId)
    """)
    boolean existsActiveBattleByUserId(@Param("userId") Long userId);

    @Query("SELECT b FROM BattleJpaEntity b " +
            "JOIN FETCH b.rival r " +
            "JOIN FETCH r.firstUser " +
            "JOIN FETCH r.secondUser " +
            "LEFT JOIN FETCH b.winner " +
            "WHERE r.firstUser.id = :userId OR r.secondUser.id = :userId")
    List<BattleJpaEntity> findByUserId(@Param("userId") Long userId);
}
