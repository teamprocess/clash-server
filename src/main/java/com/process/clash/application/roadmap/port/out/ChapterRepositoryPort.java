package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.entity.Chapter;

import java.util.List;
import java.util.Optional;

public interface ChapterRepositoryPort {
    void save(Chapter chapter);
    Optional<Chapter> findById(Long id);
    List<Chapter> findAll();
    List<Chapter> findAllBySectionId(Long sectionId);
}
