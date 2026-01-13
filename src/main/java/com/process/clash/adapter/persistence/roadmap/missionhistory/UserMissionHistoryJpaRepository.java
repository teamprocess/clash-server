package com.process.clash.adapter.persistence.roadmap.missionhistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMissionHistoryJpaRepository extends JpaRepository<UserMissionHistoryJpaEntity, Long> {
	List<UserMissionHistoryJpaEntity> findAllByUserId(Long userId);
}
