package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.model.entity.SectionKeyPoint;

import java.util.List;
import java.util.Optional;

public interface SectionKeyPointRepositoryPort {
    void save(SectionKeyPoint keyPoint);
    Optional<SectionKeyPoint> findById(Long id);
    List<SectionKeyPoint> findAll();
    List<SectionKeyPoint> findAllBySectionId(Long sectionId);
}
