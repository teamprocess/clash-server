package com.process.clash.adapter.persistence.roadmap.keypoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionKeyPointJpaRepository extends JpaRepository<SectionKeyPointJpaEntity, Long> {
	@Query("SELECT kp FROM SectionKeyPointJpaEntity kp WHERE kp.section.id = :sectionId ORDER BY kp.orderIndex ASC")
	List<SectionKeyPointJpaEntity> findAllBySectionId(@Param("sectionId") Long sectionId);

	@Modifying
	@Query("DELETE FROM SectionKeyPointJpaEntity kp WHERE kp.section.id = :sectionId")
	void deleteAllBySectionId(@Param("sectionId") Long sectionId);
}
