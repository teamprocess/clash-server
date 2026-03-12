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

    /**
     * 챕터의 질문을 eager-fetch하여 조회합니다.
     * 선택지(choices)는 batch fetch로 지연 로딩됩니다.
     * 챕터 상세 조회 시에만 사용하세요.
     *
     * 메타데이터만 필요한 경우 JPA 기본 findById()를 사용하세요. (Lazy Loading)
     */
    @EntityGraph(attributePaths = {"questions"})
    @Query("SELECT c FROM ChapterV2JpaEntity c WHERE c.id = :id")
    Optional<ChapterV2JpaEntity> findByIdWithQuestions(@Param("id") Long id);

    List<ChapterV2JpaEntity> findAllBySectionIdOrderByOrderIndexAsc(Long sectionId);
}
