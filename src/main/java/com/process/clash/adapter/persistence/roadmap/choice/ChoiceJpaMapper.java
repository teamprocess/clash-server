package com.process.clash.adapter.persistence.roadmap.choice;

import org.springframework.stereotype.Component;

import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaEntity;
import com.process.clash.domain.roadmap.Choice;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChoiceJpaMapper {

    public ChoiceJpaEntity toJpaEntity(Choice choice, MissionQuestionJpaEntity missionQuestionEntity) {
        return new ChoiceJpaEntity(
                choice.getId(),
                missionQuestionEntity,
                choice.getContent(),
                choice.isCorrect()
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
