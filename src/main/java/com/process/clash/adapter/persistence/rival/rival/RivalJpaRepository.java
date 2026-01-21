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
        FROM rival r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.first_user_id = :userId OR r.second_user_id = :userId)
    """, nativeQuery = true)
    int countAllByUserId(@Param("userId") Long userId);

    /**
     * 여러 유저의 라이벌 수 (group by)
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.first_user_id = ANY(:userIds) THEN r.first_user_id
                ELSE r.second_user_id
            END AS user_id,
            COUNT(*) AS count
        FROM rival r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.first_user_id = ANY(:userIds) OR r.second_user_id = ANY(:userIds))
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
        FROM rival r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.first_user_id = :userId OR r.second_user_id = :userId)
    """, nativeQuery = true)
    List<RivalJpaEntity> findAllByUserId(@Param("userId") Long userId);

    /**
     * 내 라이벌 상대방 ID 목록
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.first_user_id = :userId THEN r.second_user_id
                ELSE r.first_user_id
            END
        FROM rival r
        WHERE r.rival_linking_status = 'ACCEPTED'
          AND (r.first_user_id = :userId OR r.second_user_id = :userId)
    """, nativeQuery = true)
    List<Long> findOpponentIdsByUserId(@Param("userId") Long userId);

    /**
     * 라이벌 ID로 상대방 ID 조회
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.first_user_id = :userId THEN r.second_user_id
                ELSE r.first_user_id
            END
        FROM rival r
        WHERE r.id = :id
          AND r.rival_linking_status = 'ACCEPTED'
    """, nativeQuery = true)
    Long findOpponentIdByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    /**
     * 배틀 가능한 라이벌 조회
     */
    @Query(value = """
        SELECT
            CASE
                WHEN r.first_user_id = :userId THEN u2.id
                ELSE u1.id
            END AS rival_id,
            CASE
                WHEN r.first_user_id = :userId THEN u2.name
                ELSE u1.name
            END AS rival_name,
            CASE
                WHEN r.first_user_id = :userId THEN u2.profile_image
                ELSE u1.profile_image
            END AS rival_profile_image
        FROM rival r
        JOIN "user" u1 ON u1.id = r.first_user_id
        JOIN "user" u2 ON u2.id = r.second_user_id
        WHERE :userId IN (r.first_user_id, r.second_user_id)
          AND NOT EXISTS (
              SELECT 1
              FROM battle b
              WHERE b.rival_id = r.id
                AND b.battle_status IN ('IN_PROGRESS', 'PENDING')
          )
    """, nativeQuery = true)
    List<AbleRivalInfoForBattle> findAbleToBattleRivals(
            @Param("userId") Long userId
    );

    List<RivalJpaEntity> findByIdIn(Set<Long> ids);
}
