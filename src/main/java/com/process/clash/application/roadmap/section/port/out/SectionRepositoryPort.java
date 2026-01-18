package com.process.clash.application.roadmap.section.port.out;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepositoryPort {
    Section save(Section section);
    List<Section> saveAll(List<Section> sections);
    Optional<Section> findById(Long id);
    List<Section> findAll();
    List<Section> findAllById(List<Long> ids);
    List<Section> findAllByMajor(Major major);
    void deleteById(Long id);
}
