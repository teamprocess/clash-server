package com.process.clash.application.roadmap.v2.port.out;

import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

import java.util.List;
import java.util.Optional;

public interface ChapterV2RepositoryPort {
    ChapterV2 save(ChapterV2 chapter);
    
    /**
     * 챕터 메타데이터만 조회합니다. (Lazy Loading)
     * orderIndex, title, description 등의 필드만 필요한 경우 사용하세요.
     */
    Optional<ChapterV2> findById(Long id);
    
    /**
     * 챕터의 모든 질문과 선택지를 함께 조회합니다. (Eager Loading)
     * 챕터 상세 조회 시에만 사용하세요.
     */
    Optional<ChapterV2> findByIdWithQuestionsAndChoices(Long id);
    
    List<ChapterV2> findAll();
    List<ChapterV2> findAllBySectionId(Long sectionId);
    void deleteById(Long id);
}
