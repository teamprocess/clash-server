package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.roadmap.MissionQuestion;
import com.process.clash.domain.roadmap.Choice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MissionQuestionJpaMapper {

    private final ChoiceJpaMapper choiceJpaMapper;

    public MissionQuestionJpaMapper(ChoiceJpaMapper choiceJpaMapper) {
        this.choiceJpaMapper = choiceJpaMapper;
    }

    public MissionQuestionJpaEntity toJpaEntity(MissionQuestion question) {
        MissionQuestionJpaEntity e = question.getId() != null ? MissionQuestionJpaEntity.ofId(question.getId()) : new MissionQuestionJpaEntity();
        e.setContent(question.getContent());
        e.setExplanation(question.getExplanation());
        if (question.getMissionId() != null) {
            e.setMission(MissionJpaEntity.ofId(question.getMissionId()));
        }
        if (question.getChoices() != null) {
            List<ChoiceJpaEntity> choiceEntities = question.getChoices().stream().map(choiceJpaMapper::toJpaEntity).toList();
            // ensure each choice points to this question entity by id if possible
            e.setChoices(choiceEntities);
        }
        return e;
    }

    public MissionQuestion toDomain(MissionQuestionJpaEntity entity) {
        return new MissionQuestion(
                entity.getId(),
                entity.getMission() != null ? entity.getMission().getId() : null,
                entity.getContent(),
                entity.getExplanation(),
                entity.getChoices().stream().map(choiceJpaMapper::toDomain).collect(Collectors.toList())
        );
    }
}
