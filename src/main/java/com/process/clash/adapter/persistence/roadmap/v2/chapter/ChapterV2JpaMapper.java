package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaMapper;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChapterV2JpaMapper {

    private final QuestionV2JpaMapper questionV2JpaMapper;

    public ChapterV2JpaEntity toEntity(ChapterV2 chapter, SectionJpaEntity sectionEntity) {
        // 먼저 빈 questions 리스트로 Chapter 엔티티 생성
        ChapterV2JpaEntity chapterEntity = new ChapterV2JpaEntity(
                chapter.getId(),
                sectionEntity,
                chapter.getTitle(),
                chapter.getDescription(),
                chapter.getOrderIndex(),
                new ArrayList<>(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );

        List<QuestionV2JpaEntity> questions = Optional.ofNullable(chapter.getQuestions()).orElse(Collections.emptyList())
                .stream().map(q -> questionV2JpaMapper.toJpaEntity(q, chapterEntity)).toList();

        // questions 리스트를 기존의 리스트에 추가
        chapterEntity.getQuestions().addAll(questions);

        return chapterEntity;
    }

    public ChapterV2 toDomain(ChapterV2JpaEntity entity) {
        return new ChapterV2(
                entity.getId(),
                entity.getSection() != null ? entity.getSection().getId() : null,
                entity.getTitle(),
                entity.getDescription(),
                entity.getOrderIndex(),
                Optional.ofNullable(entity.getQuestions()).orElse(Collections.emptyList())
                        .stream().map(questionV2JpaMapper::toDomain).toList()
        );
    }
}
