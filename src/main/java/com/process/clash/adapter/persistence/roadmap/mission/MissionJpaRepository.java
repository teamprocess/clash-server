package com.process.clash.adapter.persistence.roadmap.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionJpaRepository extends JpaRepository<MissionJpaEntity, Long> {
	List<MissionJpaEntity> findAllByChapterId(Long chapterId);
}
