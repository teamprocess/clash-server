package com.process.clash.adapter.persistence.roadmap.choice;

import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaEntity;
import com.process.clash.domain.roadmap.entity.Choice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChoiceJpaMapper {

    public ChoiceJpaEntity toJpaEntity(Choice choice, MissionQuestionJpaEntity missionQuestionEntity) {
        return new ChoiceJpaEntity(
                choice.getId(),
                missionQuestionEntity,
                choice.getContent(),
                choice.isCorrect(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );
    }

    public Choice toDomain(ChoiceJpaEntity entity) {
        return new Choice(
                entity.getId(),
                entity.getQuestion() != null ? entity.getQuestion().getId() : null,
                entity.getContent(),
                entity.isCorrect()
        );
    }
}
