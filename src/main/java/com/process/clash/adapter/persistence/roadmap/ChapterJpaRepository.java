package com.process.clash.adapter.persistence.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterJpaRepository extends JpaRepository<ChapterJpaEntity, Long> {
	List<ChapterJpaEntity> findAllBySectionId(Long sectionId);
}