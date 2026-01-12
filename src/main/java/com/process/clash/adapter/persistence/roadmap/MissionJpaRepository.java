package com.process.clash.adapter.persistence.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionJpaRepository extends JpaRepository<MissionJpaEntity, Long> {
	List<MissionJpaEntity> findAllByChapterId(Long chapterId);
}
