package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SectionJpaRepository extends JpaRepository<SectionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"category", "prerequisites"})
    Optional<SectionJpaEntity> findById(Long id);

    @EntityGraph(attributePaths = {"chapters", "keyPoints", "prerequisites"})
    List<SectionJpaEntity> findAll();

    @EntityGraph(attributePaths = {"chapters", "keyPoints", "prerequisites"})
    List<SectionJpaEntity> findAllById(Iterable<Long> ids);

    @Query("SELECT section FROM SectionJpaEntity section WHERE section.id IN :ids")
    List<SectionJpaEntity> findAllReferencesById(Collection<Long> ids);

    @EntityGraph(attributePaths = {"category"})
    List<SectionJpaEntity> findAllByMajorOrderByOrderIndexAsc(Major major);

    boolean existsByCategoryId(Long categoryId);
}
