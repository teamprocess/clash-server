package com.process.clash.adapter.persistence.roadmap.chapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterJpaRepository extends JpaRepository<ChapterJpaEntity, Long> {
	List<ChapterJpaEntity> findAllBySectionId(Long sectionId);
}