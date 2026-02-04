package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterV2JpaRepository extends JpaRepository<ChapterV2JpaEntity, Long> {

    @EntityGraph(attributePaths = {"questions.choices"})
    Optional<ChapterV2JpaEntity> findById(Long id);

    List<ChapterV2JpaEntity> findAllBySectionId(Long sectionId);
}
