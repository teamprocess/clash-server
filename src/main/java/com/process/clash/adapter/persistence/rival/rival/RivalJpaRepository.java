package com.process.clash.adapter.persistence.rival.rival;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public interface RivalJpaRepository extends JpaRepository<RivalJpaEntity, Long> {

    /**
     * 내 현재 라이벌 수
     */
    @Query(value = """
        SELECT COUNT(*)
        FROM rivals r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
    """, nativeQuery = true)
    int countAllByUserId(@Param("userId") Long userId);

    /**
     * 여러 유저의 라이벌 수 (group by)
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.fk_first_user_id IN (:userIds) THEN r.fk_first_user_id
                ELSE r.fk_second_user_id
            END AS user_id,
            COUNT(*) AS count
        FROM rivals r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.fk_first_user_id IN (:userIds) OR r.fk_second_user_id IN (:userIds))
        GROUP BY user_id
    """, nativeQuery = true)
    List<Map<String, Object>> countAllByUserIdsGrouped(
            @Param("userIds") List<Long> userIds
    );

    /**
     * 내 라이벌 전체 조회
     */
    @Query(value = """
        SELECT *
        FROM rivals r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
    """, nativeQuery = true)
    List<RivalJpaEntity> findAllByUserId(@Param("userId") Long userId);

    /**
     * 내 라이벌 상대방 ID 목록
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.fk_first_user_id = :userId THEN r.fk_second_user_id
                ELSE r.fk_first_user_id
            END
        FROM rivals r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.fk_first_user_id = :userId OR r.fk_second_user_id = :userId)
    """, nativeQuery = true)
    List<Long> findOpponentIdsByUserId(@Param("userId") Long userId);

    /**
     * 라이벌 ID로 상대방 ID 조회
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.fk_first_user_id = :userId THEN r.fk_second_user_id
                ELSE r.fk_first_user_id
            END
        FROM rivals r
        WHERE r.id = :id
          AND r.rival_linking_status = 'ACCEPTED'
    """, nativeQuery = true)
    Long findOpponentIdByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    @Query(value = """
        SELECT
            CASE
                WHEN r.fk_first_user_id = :userId THEN r.fk_second_user_id
                ELSE r.fk_first_user_id
            END
        FROM rivals r
        WHERE r.id = :id
    """, nativeQuery = true)
    Long findOpponentIdByIdAndUserIdInRejectCase(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    /**
     * 배틀 가능한 라이벌 조회
     */
    @Query("""
        select new com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle(
            case when r.firstUser.id = :userId then r.secondUser.id else r.firstUser.id end,
            case when r.firstUser.id = :userId then r.secondUser.name else r.firstUser.name end,
            case when r.firstUser.id = :userId then r.secondUser.profileImage else r.firstUser.profileImage end
        )
        from RivalJpaEntity r
        where :userId in (r.firstUser.id, r.secondUser.id)
            and r.rivalLinkingStatus = 'ACCEPTED'
            and r.id not in (
                select b.rival.id
                from BattleJpaEntity b
                where b.battleStatus not in ('DONE', 'REJECT')
                    and :userId in (b.rival.firstUser.id, b.rival.secondUser.id)
            )
    """)
    List<AbleRivalInfoForBattle> findAbleToBattleRivals(@Param("userId") Long userId);

    List<RivalJpaEntity> findByIdIn(Set<Long> ids);
}