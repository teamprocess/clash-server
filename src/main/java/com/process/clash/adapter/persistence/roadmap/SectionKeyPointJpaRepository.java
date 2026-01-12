package com.process.clash.adapter.persistence.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionKeyPointJpaRepository extends JpaRepository<SectionKeyPointJpaEntity, Long> {
	List<SectionKeyPointJpaEntity> findAllBySectionId(Long sectionId);
}
