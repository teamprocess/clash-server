package com.process.clash.application.major.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.major.entity.MajorQuestion;

import java.time.LocalDateTime;

public class UpdateMajorQuestionData {

    public record Command(Actor actor, Long questionId, String content, WeightVo weight) {

        public record WeightVo(Integer web, Integer app, Integer server, Integer ai, Integer game) {
            public MajorQuestion.WeightVo toDomain() {
                return new MajorQuestion.WeightVo(web, app, server, ai, game);
            }
        }
    }

    public record Result(
            Long questionId,
            String content,
            WeightVo weight,
            LocalDateTime updatedAt
    ) {

        public static Result from(MajorQuestion domain) {
            return new Result(
                    domain.getId(),
                    domain.getContent(),
                    WeightVo.from(domain.getWeightVo()),
                    domain.getUpdatedAt()
            );
        }

        public record WeightVo(
                Integer web,
                Integer app,
                Integer server,
                Integer ai,
                Integer game
        ) {

            public static WeightVo from(MajorQuestion.WeightVo domainWeight) {
                return new WeightVo(
                        domainWeight.getWeb(),
                        domainWeight.getApp(),
                        domainWeight.getServer(),
                        domainWeight.getAi(),
                        domainWeight.getGame()
                );
            }
        }
    }
}
