package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.model.entity.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepositoryPort {
    void save(Section section);
    Optional<Section> findById(Long id);
    List<Section> findAll();
    List<Section> findAllByMajor(Major major);
}
