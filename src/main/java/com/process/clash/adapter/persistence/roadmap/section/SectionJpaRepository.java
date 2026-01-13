package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionJpaRepository extends JpaRepository<SectionJpaEntity, Long> {

    List<SectionJpaEntity> findAllByMajor(Major major);
}
