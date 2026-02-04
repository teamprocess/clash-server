package com.process.clash.application.roadmap.v2.port.out;

import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

import java.util.List;
import java.util.Optional;

public interface ChapterV2RepositoryPort {
    ChapterV2 save(ChapterV2 chapter);
    Optional<ChapterV2> findById(Long id);
    List<ChapterV2> findAll();
    List<ChapterV2> findAllBySectionId(Long sectionId);
    void deleteById(Long id);
}
