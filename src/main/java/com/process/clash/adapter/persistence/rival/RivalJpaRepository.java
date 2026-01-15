package com.process.clash.adapter.persistence.rival;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RivalJpaRepository extends JpaRepository<RivalJpaEntity, Long> {

    @Query("""
        select count(r)
        from RivalJpaEntity as r
        where r.my.id = :myId
            and r.rivalLinkingStatus = 'ACCEPTED'
    """)
    int countAllByMy_Id(
            @Param("myId") Long myId
    );

    @Query("""
        select count(r)
        from RivalJpaEntity as r
        where r.opponent.id = :opponentId
            and r.rivalLinkingStatus = 'ACCEPTED'
    """)
    int countAllByOpponent_Id(
            @Param("opponentId") Long opponentId
    );
}
