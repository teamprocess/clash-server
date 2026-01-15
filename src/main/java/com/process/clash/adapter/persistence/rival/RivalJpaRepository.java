package com.process.clash.adapter.persistence.rival;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RivalJpaRepository extends JpaRepository<RivalJpaEntity, Long> {

    int countAllByMy_Id(Long myId);
    int countAllByOpponent_Id(Long opponentId);
}
