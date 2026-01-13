package com.process.clash.adapter.persistence.roadmap.missionquestion;

import com.process.clash.adapter.persistence.roadmap.choice.ChoiceJpaMapper;
import com.process.clash.domain.roadmap.MissionQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionQuestionJpaMapper {

    private final ChoiceJpaMapper choiceJpaMapper;

    public MissionQuestionJpaEntity toJpaEntity(MissionQuestion question) {
        return null;
    }

    public MissionQuestion toDomain(MissionQuestionJpaEntity entity) {
        return null;
    }
}
