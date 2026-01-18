package com.process.clash.adapter.persistence.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
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
    int countAllByMy_Id(
            @Param("myId") Long myId
    );

    // 상대방 현재 라이벌 수
    @Query("""
        select count(r)
        from RivalJpaEntity as r
        where r.opponent.id = :opponentId
            and r.rivalLinkingStatus = 'ACCEPTED'
    """)
    int countAllByOpponent_Id(
            @Param("opponentId") Long opponentId
    );

    List<RivalJpaEntity> findAllByMy_Id(Long myId);

    Long my(UserJpaEntity my);
}
