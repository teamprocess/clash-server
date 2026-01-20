package com.process.clash.adapter.persistence.rival;

import com.process.clash.application.compete.rival.data.RivalInfoForGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RivalJpaRepository extends JpaRepository<RivalJpaEntity, Long> {

    // 내 현재 라이벌 수
    @Query("""
        select count(r)
        from RivalJpaEntity as r
        where r.my.id = :myId
            and r.rivalLinkingStatus = 'ACCEPTED'
    """)
    int countAllByMyId(
            @Param("myId") Long myId
    );

    // 상대방 현재 라이벌 수
    @Query("""
    select new map(r.opponent.id as opponentId, count(r) as count)
    from RivalJpaEntity as r
    where r.rivalLinkingStatus = 'ACCEPTED'
        and r.opponent.id in :opponentIds
    group by r.opponent.id
""")
    List<Map<String, Object>> countAllByOpponentIdsGrouped(
            @Param("opponentIds") List<Long> opponentIds
    );

    List<RivalJpaEntity> findAllByMyId(Long myId);

    @Query("""
        select new com.process.clash.application.compete.rival.data.RivalInfoForGraph(
            r.opponent.id,
            r.opponent.name
        )
        from RivalJpaEntity r
        where r.my.id = :myId
            and r.rivalLinkingStatus = 'ACCEPTED'
    """)
    List<RivalInfoForGraph> findRivalInfoForGraphByMyId(
            @Param("myId") Long myId
    );
}
