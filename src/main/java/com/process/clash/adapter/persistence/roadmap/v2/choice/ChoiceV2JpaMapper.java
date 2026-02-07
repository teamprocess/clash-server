package com.process.clash.adapter.persistence.roadmap.v2.choice;

import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaEntity;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChoiceV2JpaMapper {

    public ChoiceV2JpaEntity toJpaEntity(ChoiceV2 choice, QuestionV2JpaEntity questionEntity) {
        return new ChoiceV2JpaEntity(
                choice.getId(),
                questionEntity,
                choice.getContent(),
                choice.isCorrect(),
                choice.getOrderIndex(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );
    }

    public ChoiceV2 toDomain(ChoiceV2JpaEntity entity) {
        return new ChoiceV2(
                entity.getId(),
                entity.getQuestion() != null ? entity.getQuestion().getId() : null,
                entity.getContent(),
                entity.isCorrect(),
                entity.getOrderIndex()
        );
    }
}
