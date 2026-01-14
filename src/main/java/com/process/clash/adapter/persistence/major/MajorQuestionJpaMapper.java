package com.process.clash.adapter.persistence.major;

import com.process.clash.domain.major.entity.MajorQuestion;
import org.springframework.stereotype.Component;

@Component
public class MajorQuestionJpaMapper {
    public MajorQuestionJpaEntity toJpaEntity(MajorQuestion majorQuestion) {

        return new MajorQuestionJpaEntity(
                majorQuestion.getId(),
                majorQuestion.getContent(),
                new MajorQuestionJpaEntity.WeightVo(
                        majorQuestion.getWeightVo().getWeb(),
                        majorQuestion.getWeightVo().getApp(),
                        majorQuestion.getWeightVo().getServer(),
                        majorQuestion.getWeightVo().getAi(),
                        majorQuestion.getWeightVo().getGame()
                ),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );
    }

    public MajorQuestion toDomain(MajorQuestionJpaEntity majorQuestionJpaEntity) {

        return new MajorQuestion(
                majorQuestionJpaEntity.getId(),
                majorQuestionJpaEntity.getContent(),
                new MajorQuestion.WeightVo(
                        majorQuestionJpaEntity.getWeightVo().getWeb(),
                        majorQuestionJpaEntity.getWeightVo().getApp(),
                        majorQuestionJpaEntity.getWeightVo().getServer(),
                        majorQuestionJpaEntity.getWeightVo().getAi(),
                        majorQuestionJpaEntity.getWeightVo().getGame()
                ),
                majorQuestionJpaEntity.getCreatedAt()
        );
    }
}
