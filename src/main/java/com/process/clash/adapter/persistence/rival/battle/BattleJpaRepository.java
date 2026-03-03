package com.process.clash.adapter.persistence.rival.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BattleJpaRepository extends JpaRepository<BattleJpaEntity, Long> {

    /**
     * 유저가 진행 중인 배틀이 있는지 확인
     */
    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM battles b
        LEFT JOIN rivals r ON b.fk_rival_id = r.id
        WHERE b.battle_status not in ('REJECTED', 'PENDING')
          AND (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
    """, nativeQuery = true)
    boolean existsActiveBattleByUserId(@Param("userId") Long userId);

    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM battles b
        WHERE b.battle_status NOT IN ('DONE', 'REJECTED')
          AND (b.fk_rival_id = :rivalId)
            AND b.id NOT IN (
                    SELECT ba.id
                    FROM battles ba
                    WHERE ba.fk_rival_id = :rivalId
                        AND ba.battle_status = 'PENDING'
                )
    """, nativeQuery = true)
    boolean existsActiveBattleByRivalId(@Param("rivalId") Long rivalId);

    /**
     * 유저 관련 모든 배틀 조회
     */
    @Query(value = """
        SELECT b.*
        FROM battles b
        LEFT JOIN rivals r ON b.fk_rival_id = r.id
        WHERE (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
            AND b.battle_status not in ('REJECTED', 'PENDING')
    """, nativeQuery = true)
    List<BattleJpaEntity> findByUserIdWithOutRejected(@Param("userId") Long userId);

    /**
     * 라이벌에 대한 PENDING 배틀 신청 존재 여부
     */
    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM battles b
        WHERE b.fk_rival_id = :rivalId
          AND b.battle_status = 'PENDING'
    """, nativeQuery = true)
    boolean existsPendingBattleByRivalId(@Param("rivalId") Long rivalId);

    /**
     * 유저 탈퇴 시 해당 유저가 속한 모든 진행 중인 배틀 REJECTED 처리
     */
    @Modifying
    @Query(value = """
        UPDATE battles b
        SET b.battle_status = 'REJECTED'
        FROM rivals r
        WHERE b.fk_rival_id = r.id
          AND (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
          AND b.battle_status NOT IN ('DONE', 'REJECTED')
    """, nativeQuery = true)
    void rejectAllActiveBattlesByUserId(@Param("userId") Long userId);

    /**
     * 유저 관련 진행 중인 배틀 조회 (Optional)
     */
    @Query(value = """
        SELECT b.*
        FROM battles b
        LEFT JOIN rivals r ON b.fk_rival_id = r.id
        WHERE b.battle_status NOT IN ('DONE', 'REJECTED')
          AND (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
        LIMIT 1
    """, nativeQuery = true)
    Optional<BattleJpaEntity> findActiveByUserId(@Param("userId") Long userId);

    /**
     * 종료일이 지났으나 아직 IN_PROGRESS 상태인 배틀 조회 (스케줄러용)
     */
    @Query(value = """
        SELECT b.*
        FROM battles b
        WHERE b.battle_status = 'IN_PROGRESS'
          AND b.end_date < :today
    """, nativeQuery = true)
    List<BattleJpaEntity> findExpiredInProgressBattles(@Param("today") LocalDate today);
}
