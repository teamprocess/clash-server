package com.process.clash.adapter.persistence.roadmap.chapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaRepository;
import com.process.clash.application.roadmap.port.out.ChapterRepositoryPort;
import com.process.clash.domain.roadmap.Chapter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChapterPersistenceAdapter implements ChapterRepositoryPort {

    private final ChapterJpaRepository chapterJpaRepository;
    private final ChapterJpaMapper chapterJpaMapper;
    private final SectionJpaRepository sectionJpaRepository;

    @Override
    public void save(Chapter chapter) {
        SectionJpaEntity sectionEntity = sectionJpaRepository.getReferenceById(chapter.getSectionId());
        chapterJpaRepository.save(chapterJpaMapper.toEntity(chapter, sectionEntity));
    }

    @Override
    public Optional<Chapter> findById(Long id) {
        return chapterJpaRepository.findById(id).map(chapterJpaMapper::toDomain);
    }

    @Override
    public List<Chapter> findAll() {
        return chapterJpaRepository.findAll().stream().map(chapterJpaMapper::toDomain).toList();
    }

    @Override
    public List<Chapter> findAllBySectionId(Long sectionId) {
        return chapterJpaRepository.findAllBySectionId(sectionId).stream().map(chapterJpaMapper::toDomain).toList();
    }
}
