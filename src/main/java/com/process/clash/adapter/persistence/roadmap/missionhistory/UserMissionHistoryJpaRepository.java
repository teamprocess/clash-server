package com.process.clash.adapter.persistence.roadmap.missionhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMissionHistoryJpaRepository extends JpaRepository<UserMissionHistoryJpaEntity, Long> {
	List<UserMissionHistoryJpaEntity> findAllByUserId(Long userId);
	Optional<UserMissionHistoryJpaEntity> findByUserIdAndMissionId(Long userId, Long missionId);
	List<UserMissionHistoryJpaEntity> findAllByUserIdAndMissionIdIn(Long userId, List<Long> missionIds);
}
