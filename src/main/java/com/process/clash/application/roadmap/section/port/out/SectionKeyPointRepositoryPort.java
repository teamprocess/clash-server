package com.process.clash.application.roadmap.section.port.out;

import com.process.clash.domain.roadmap.entity.SectionKeyPoint;

import java.util.List;
import java.util.Optional;

public interface SectionKeyPointRepositoryPort {
    void save(SectionKeyPoint keyPoint);
    Optional<SectionKeyPoint> findById(Long id);
    List<SectionKeyPoint> findAll();
    List<SectionKeyPoint> findAllBySectionId(Long sectionId);
}
