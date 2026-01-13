package com.process.clash.adapter.persistence.roadmap.missionquestion;

import com.process.clash.adapter.persistence.roadmap.choice.ChoiceJpaMapper;
import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaRepository;
import com.process.clash.domain.roadmap.MissionQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MissionQuestionJpaMapper {

    private final ChoiceJpaMapper choiceJpaMapper;
    private final MissionJpaRepository missionJpaRepository;

    public MissionQuestionJpaEntity toJpaEntity(MissionQuestion question) {
        return new MissionQuestionJpaEntity(
                question.getId(),
                missionJpaRepository.getReferenceById(question.getMissionId()),
                question.getContent(),
                question.getExplanation(),
                Optional.ofNullable(question.getChoices()).orElse(Collections.emptyList())
                        .stream().map(choiceJpaMapper::toJpaEntity).toList()
        );
    }

    public MissionQuestion toDomain(MissionQuestionJpaEntity entity) {
        return new MissionQuestion(
                entity.getId(),
                entity.getMission().getId(),
                entity.getContent(),
                entity.getExplanation(),
                entity.getChoices().stream().map(choiceJpaMapper::toDomain).toList()
        );
    }
}
