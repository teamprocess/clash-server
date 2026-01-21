package com.process.clash.adapter.persistence.rival.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BattleJpaRepository extends JpaRepository<BattleJpaEntity, Long> {

    /**
     * 유저가 진행 중인 배틀이 있는지 확인
     */
    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM battle b
        JOIN rival r ON b.rival_id = r.id
        WHERE b.battle_status <> 'DONE'
          AND (r.first_user_id = :userId OR r.second_user_id = :userId)
    """, nativeQuery = true)
    boolean existsActiveBattleByUserId(@Param("userId") Long userId);

    /**
     * 유저 관련 모든 배틀 조회
     */
    @Query(value = """
        SELECT b.*
        FROM battle b
        JOIN rival r ON b.rival_id = r.id
        WHERE r.first_user_id = :userId OR r.second_user_id = :userId
    """, nativeQuery = true)
    List<BattleJpaEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 유저 관련 진행 중인 배틀 조회 (Optional)
     */
    @Query(value = """
        SELECT b.*
        FROM battle b
        JOIN rival r ON b.rival_id = r.id
        WHERE b.battle_status <> 'DONE'
          AND (r.first_user_id = :userId OR r.second_user_id = :userId)
        LIMIT 1
    """, nativeQuery = true)
    Optional<BattleJpaEntity> findActiveByUserId(@Param("userId") Long userId);
}
