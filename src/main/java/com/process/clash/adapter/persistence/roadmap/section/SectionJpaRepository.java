package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionJpaRepository extends JpaRepository<SectionJpaEntity, Long> {

    List<SectionJpaEntity> findAllByMajorOrderByOrderIndexAsc(Major major);
}
