package com.process.clash.adapter.persistence.group;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupJpaRepository extends JpaRepository<GroupJpaEntity, Long> {
}