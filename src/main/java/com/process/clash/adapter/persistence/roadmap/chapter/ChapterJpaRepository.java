package com.process.clash.adapter.persistence.roadmap.chapter;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterJpaRepository extends JpaRepository<ChapterJpaEntity, Long> {

	@EntityGraph(attributePaths = {"missions.questions.choices"})
	Optional<ChapterJpaEntity> findById(Long id);

	List<ChapterJpaEntity> findAllBySectionIdOrderByOrderIndexAsc(Long sectionId);
}