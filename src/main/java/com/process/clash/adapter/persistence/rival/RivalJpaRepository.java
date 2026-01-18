package com.process.clash.adapter.persistence.rival;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        select count(r)
        from RivalJpaEntity as r
        where r.opponent.id = :opponentId
            and r.rivalLinkingStatus = 'ACCEPTED'
    """)
    int countAllByOpponentId(
            @Param("opponentId") Long opponentId
    );

    List<RivalJpaEntity> findAllByMyId(Long myId);
}
