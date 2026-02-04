package com.process.clash.adapter.persistence.roadmap.v2.question;

import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.choice.ChoiceV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.choice.ChoiceV2JpaMapper;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionV2JpaMapper {

    private final ChoiceV2JpaMapper choiceV2JpaMapper;

    public QuestionV2JpaEntity toJpaEntity(QuestionV2 question, ChapterV2JpaEntity chapterEntity) {
        QuestionV2JpaEntity questionEntity = new QuestionV2JpaEntity(
                question.getId(),
                chapterEntity,
                question.getContent(),
                question.getExplanation(),
                question.getOrderIndex(),
                question.getDifficulty(),
                new ArrayList<>(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );

        // null-safe: question.getChoices()가 null이면 빈 리스트로 처리
        List<ChoiceV2JpaEntity> choices = (question.getChoices() != null)
                ? question.getChoices().stream()
                .map(c -> choiceV2JpaMapper.toJpaEntity(c, questionEntity)).toList() :
                new ArrayList<>();

        questionEntity.getChoices().addAll(choices);

        return questionEntity;
    }

    public QuestionV2 toDomain(QuestionV2JpaEntity entity) {
        return new QuestionV2(
                entity.getId(),
                entity.getChapter() != null ? entity.getChapter().getId() : null,
                entity.getContent(),
                entity.getExplanation(),
                entity.getOrderIndex(),
                entity.getDifficulty(),
                // null-safe: entity.getChoices()가 null이면 빈 리스트로 처리
                (entity.getChoices() != null)
                        ? entity.getChoices().stream()
                        .map(choiceV2JpaMapper::toDomain).toList() :
                        new ArrayList<>()
        );
    }
}
