package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionJpaRepository extends JpaRepository<SectionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"prerequisites"})
    Optional<SectionJpaEntity> findById(Long id);

    @EntityGraph(attributePaths = {"prerequisites"})
    List<SectionJpaEntity> findAll();

    List<SectionJpaEntity> findAllByMajorOrderByOrderIndexAsc(Major major);
}
