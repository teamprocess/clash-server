package com.process.clash.adapter.persistence.rival.rival;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
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
    @Query("""
        select count(r)
        from RivalJpaEntity r
        where r.rivalLinkingStatus = 'ACCEPTED'
          and (r.firstUser.id = :userId or r.secondUser.id = :userId)
    """)
    int countAllByUserId(
            @Param("userId") Long userId
    );

    /**
     * 여러 유저의 라이벌 수 (group by)
     */
    @Query("""
        select new map(
            case
                when r.firstUser.id in :userIds then r.firstUser.id
                else r.secondUser.id
            end as userId,
            count(r) as count
        )
        from RivalJpaEntity r
        where r.rivalLinkingStatus = 'ACCEPTED'
          and (r.firstUser.id in :userIds or r.secondUser.id in :userIds)
        group by
            case
                when r.firstUser.id in :userIds then r.firstUser.id
                else r.secondUser.id
            end
    """)
    List<Map<String, Object>> countAllByUserIdsGrouped(
            @Param("userIds") List<Long> userIds
    );

    /**
     * 내 라이벌 전체 조회
     */
    @Query("""
        select r
        from RivalJpaEntity r
        where r.rivalLinkingStatus = 'ACCEPTED'
          and (r.firstUser.id = :userId or r.secondUser.id = :userId)
    """)
    List<RivalJpaEntity> findAllByUserId(
            @Param("userId") Long userId
    );

    /**
     * 내 라이벌 상대방 ID 목록
     */
    @Query("""
        select
            case
                when r.firstUser.id = :userId then r.secondUser.id
                else r.firstUser.id
            end
        from RivalJpaEntity r
        where r.rivalLinkingStatus = 'ACCEPTED'
          and (r.firstUser.id = :userId or r.secondUser.id = :userId)
    """)
    List<Long> findOpponentIdsByUserId(
            @Param("userId") Long userId
    );

    @Query("""
        select
            case
                when r.firstUser.id = :userId then r.secondUser.id
                else r.firstUser.id
            end
        from RivalJpaEntity as r
        where r.rivalLinkingStatus = 'ACCEPTED'
            and r.id = :id
    """)
    Long findOpponentIdByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    @Query("""
        select new com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle(
            case 
                when r.firstUser.id = :userId then r.secondUser.id
                else r.firstUser.id
            end,
            case 
                when r.firstUser.id = :userId then r.secondUser.name
                else r.firstUser.name
            end,
            case 
                when r.firstUser.id = :userId then r.secondUser.profileImage
                else r.firstUser.profileImage
            end
        )
        from RivalJpaEntity r
        where :userId in (r.firstUser.id, r.secondUser.id)
          and not exists (
              select 1
              from BattleJpaEntity b
              where b.battleStatus in (
                  com.process.clash.domain.rival.battle.enums.BattleStatus.IN_PROGRESS,
                  com.process.clash.domain.rival.battle.enums.BattleStatus.PENDING
              )
              and (
                  (r.firstUser.id = :userId and (
                        b.rival.firstUser.id = r.secondUser.id
                     or b.rival.secondUser.id = r.secondUser.id
                  ))
                  or
                  (r.secondUser.id = :userId and (
                        b.rival.firstUser.id = r.firstUser.id
                     or b.rival.secondUser.id = r.firstUser.id
                  ))
              )
          )
    """)
    List<AbleRivalInfoForBattle> findAbleToBattleRivals(@Param("userId") Long userId);

    List<RivalJpaEntity> findByIdIn(Set<Long> ids);
}
