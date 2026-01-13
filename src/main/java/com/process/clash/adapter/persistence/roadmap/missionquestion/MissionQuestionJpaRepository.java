package com.process.clash.adapter.persistence.roadmap.missionquestion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionQuestionJpaRepository extends JpaRepository<MissionQuestionJpaEntity, Long> {
	List<MissionQuestionJpaEntity> findAllByMissionId(Long missionId);
}
