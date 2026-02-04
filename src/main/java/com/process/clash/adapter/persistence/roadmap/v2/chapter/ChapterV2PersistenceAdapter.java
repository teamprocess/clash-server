package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaRepository;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChapterV2PersistenceAdapter implements ChapterV2RepositoryPort {

    private final ChapterV2JpaRepository chapterV2JpaRepository;
    private final ChapterV2JpaMapper chapterV2JpaMapper;
    private final SectionJpaRepository sectionJpaRepository;

    @Override
    public ChapterV2 save(ChapterV2 chapter) {
        SectionJpaEntity sectionEntity = sectionJpaRepository.getReferenceById(chapter.getSectionId());
        ChapterV2JpaEntity savedEntity = chapterV2JpaRepository.save(chapterV2JpaMapper.toEntity(chapter, sectionEntity));
        return chapterV2JpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ChapterV2> findById(Long id) {
        return chapterV2JpaRepository.findById(id).map(chapterV2JpaMapper::toDomain);
    }

    @Override
    public List<ChapterV2> findAll() {
        return chapterV2JpaRepository.findAll().stream().map(chapterV2JpaMapper::toDomain).toList();
    }

    @Override
    public List<ChapterV2> findAllBySectionId(Long sectionId) {
        return chapterV2JpaRepository.findAllBySectionId(sectionId).stream().map(chapterV2JpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        chapterV2JpaRepository.deleteById(id);
    }
}
