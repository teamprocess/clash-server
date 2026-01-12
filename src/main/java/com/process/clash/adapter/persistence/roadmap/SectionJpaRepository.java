package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionJpaRepository extends JpaRepository<SectionJpaEntity, Long> {

    List<SectionJpaEntity> findAllByMajor(Major major);
}
