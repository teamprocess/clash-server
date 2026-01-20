package com.process.clash.adapter.persistence.rival.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BattleJpaRepository extends JpaRepository<BattleJpaEntity, Long> {
}
