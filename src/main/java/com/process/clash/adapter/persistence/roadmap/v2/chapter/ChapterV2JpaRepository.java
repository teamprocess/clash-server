package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterV2JpaRepository extends JpaRepository<ChapterV2JpaEntity, Long> {

    @EntityGraph(attributePaths = {"questions.choices"})
    Optional<ChapterV2JpaEntity> findById(Long id);

    /**
     * 질문 컬렉션 없이 챕터 메타데이터만 조회합니다. (성능 최적화)
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT c FROM ChapterV2JpaEntity c WHERE c.id = :id")
    Optional<ChapterV2JpaEntity> findByIdWithoutQuestions(@Param("id") Long id);

    List<ChapterV2JpaEntity> findAllBySectionId(Long sectionId);
}
