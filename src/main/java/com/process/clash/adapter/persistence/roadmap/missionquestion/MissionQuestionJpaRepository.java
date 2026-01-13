package com.process.clash.adapter.persistence.roadmap.missionquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionQuestionJpaRepository extends JpaRepository<MissionQuestionJpaEntity, Long> {
	List<MissionQuestionJpaEntity> findAllByMissionId(Long missionId);
}
