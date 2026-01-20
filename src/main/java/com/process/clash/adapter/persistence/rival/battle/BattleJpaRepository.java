package com.process.clash.adapter.persistence.rival.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
