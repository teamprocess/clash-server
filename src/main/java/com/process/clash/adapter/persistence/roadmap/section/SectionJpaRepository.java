package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionJpaRepository extends JpaRepository<SectionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"chapters", "keyPoints", "prerequisites"})
    Optional<SectionJpaEntity> findById(Long id);

    @EntityGraph(attributePaths = {"chapters", "keyPoints", "prerequisites"})
    List<SectionJpaEntity> findAll();

    @EntityGraph(attributePaths = {"chapters", "keyPoints", "prerequisites"})
    List<SectionJpaEntity> findAllById(Iterable<Long> ids);

    List<SectionJpaEntity> findAllByMajorOrderByOrderIndexAsc(Major major);
}
