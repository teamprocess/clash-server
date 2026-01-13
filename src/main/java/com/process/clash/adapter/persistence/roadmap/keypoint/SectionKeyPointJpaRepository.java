package com.process.clash.adapter.persistence.roadmap.keypoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionKeyPointJpaRepository extends JpaRepository<SectionKeyPointJpaEntity, Long> {
	List<SectionKeyPointJpaEntity> findAllBySectionId(Long sectionId);
}
