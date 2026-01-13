package com.process.clash.adapter.persistence.roadmap.missionhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMissionHistoryJpaRepository extends JpaRepository<UserMissionHistoryJpaEntity, Long> {
	List<UserMissionHistoryJpaEntity> findAllByUserId(Long userId);
}
