package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.roadmap.Choice;
import org.springframework.stereotype.Component;

@Component
public class ChoiceJpaMapper {

    public ChoiceJpaEntity toJpaEntity(Choice choice) {
        ChoiceJpaEntity e = choice.getId() != null ? ChoiceJpaEntity.ofId(choice.getId()) : new ChoiceJpaEntity();
        e.setContent(choice.getContent());
        e.setIsCorrect(choice.isCorrect());
        if (choice.getQuestionId() != null) {
            e.setQuestion(MissionQuestionJpaEntity.ofId(choice.getQuestionId()));
        }
        return e;
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
