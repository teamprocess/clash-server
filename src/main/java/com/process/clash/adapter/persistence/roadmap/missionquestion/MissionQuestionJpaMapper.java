package com.process.clash.adapter.persistence.roadmap.missionquestion;

import com.process.clash.adapter.persistence.roadmap.choice.ChoiceJpaEntity;
import com.process.clash.adapter.persistence.roadmap.choice.ChoiceJpaMapper;
import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaEntity;
import com.process.clash.domain.roadmap.entity.MissionQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MissionQuestionJpaMapper {

    private final ChoiceJpaMapper choiceJpaMapper;

    public MissionQuestionJpaEntity toJpaEntity(MissionQuestion question, MissionJpaEntity missionEntity) {
        MissionQuestionJpaEntity questionEntity = new MissionQuestionJpaEntity(
                question.getId(),
                missionEntity,
                question.getContent(),
                question.getExplanation(),
                question.getOrderIndex(),
                new ArrayList<>(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );

        // null-safe: question.getChoices()가 null이면 빈 리스트로 처리
        List<ChoiceJpaEntity> choices = (question.getChoices() != null)
                ? question.getChoices().stream()
                .map(c -> choiceJpaMapper.toJpaEntity(c, questionEntity)).toList() :
                new ArrayList<>();

        questionEntity.getChoices().addAll(choices);

        return questionEntity;
    }

    public MissionQuestion toDomain(MissionQuestionJpaEntity entity) {
        return new MissionQuestion(
                entity.getId(),
                entity.getMission() != null ? entity.getMission().getId() : null,
                entity.getContent(),
                entity.getExplanation(),
                entity.getOrderIndex(),
                // null-safe: entity.getChoices()가 null이면 빈 리스트로 처리
                (entity.getChoices() != null)
                        ? entity.getChoices().stream()
                        .map(choiceJpaMapper::toDomain).toList() :
                        new ArrayList<>()
        );
    }
}