package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaRepository;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.ChapterV2NotFoundException;
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

    /**
     * 챕터 메타데이터만 업데이트합니다. (JPA 더티 체킹 활용)
     * questions 컬렉션을 로딩하지 않아 성능이 최적화됩니다.
     */
    public ChapterV2 updateMetadata(Long chapterId, String title, String description, 
                                     Integer orderIndex, String studyMaterialUrl) {
        // JPA 기본 findById 사용 (Lazy Loading으로 questions는 로드되지 않음)
        ChapterV2JpaEntity entity = chapterV2JpaRepository.findById(chapterId)
                .orElseThrow(ChapterV2NotFoundException::new);
        
        entity.updateMetadata(title, description, orderIndex, studyMaterialUrl);
        // @Transactional에 의해 자동으로 UPDATE 쿼리 실행 (더티 체킹)
        
        return chapterV2JpaMapper.toDomain(entity);
    }

    @Override
    public Optional<ChapterV2> findById(Long id) {
        return chapterV2JpaRepository.findById(id).map(chapterV2JpaMapper::toDomain);
    }

    @Override
    public Optional<ChapterV2> findByIdWithQuestionsAndChoices(Long id) {
        return chapterV2JpaRepository.findByIdWithQuestionsAndChoices(id).map(chapterV2JpaMapper::toDomain);
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
