package com.process.clash.adapter.persistence.roadmap.choice;

import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaRepository;
import com.process.clash.domain.roadmap.Choice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChoiceJpaMapper {

    private final MissionQuestionJpaRepository missionQuestionJpaRepository;

    public ChoiceJpaEntity toJpaEntity(Choice choice) {
        return new ChoiceJpaEntity(
                choice.getId(),
                missionQuestionJpaRepository.getReferenceById(choice.getQuestionId()),
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
