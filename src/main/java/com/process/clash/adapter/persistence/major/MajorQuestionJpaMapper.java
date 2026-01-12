package com.process.clash.adapter.persistence.major;

import com.process.clash.domain.major.MajorQuestion;

public class MajorQuestionJpaMapper {
    public static MajorQuestionJpaEntity toJpaEntity(MajorQuestion majorQuestion) {
        return new MajorQuestionJpaEntity(
                majorQuestion.getId(),
                majorQuestion.getContent(),
                majorQuestion.getMajor(),
                new MajorQuestionJpaEntity.WeightVo(
                        majorQuestion.getWeightVo().getWeb(),
                        majorQuestion.getWeightVo().getApp(),
                        majorQuestion.getWeightVo().getServer(),
                        majorQuestion.getWeightVo().getAi(),
                        majorQuestion.getWeightVo().getGame()
                )
        );
    }

    public static MajorQuestion toDomain(MajorQuestionJpaEntity majorQuestionJpaEntity) {

        return new MajorQuestion(
                majorQuestionJpaEntity.getId(),
                majorQuestionJpaEntity.getContent(),
                majorQuestionJpaEntity.getMajor(),
                new MajorQuestion.WeightVo(
                        majorQuestionJpaEntity.getWeightVo().getWeb(),
                        majorQuestionJpaEntity.getWeightVo().getApp(),
                        majorQuestionJpaEntity.getWeightVo().getServer(),
                        majorQuestionJpaEntity.getWeightVo().getAi(),
                        majorQuestionJpaEntity.getWeightVo().getGame()
                )
        );
    }
}
