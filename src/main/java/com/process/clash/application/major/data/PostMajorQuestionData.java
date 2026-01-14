package com.process.clash.application.major.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.major.entity.MajorQuestion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostMajorQuestionData {

    public record Command(Actor actor, String content, PostMajorQuestionData.Command.WeightVo weight) {

        public record WeightVo(Integer web, Integer app, Integer server, Integer ai, Integer game) {
            public MajorQuestion.WeightVo toDomain() {
                return new MajorQuestion.WeightVo(web, app, server, ai, game);
            }
        }

        public MajorQuestion toDomain() {
            return new MajorQuestion(null, content, weight.toDomain(), null);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final Long questionId;
        private final String content;
        private final WeightVo weight;
        private final LocalDateTime createdAt;

        public static Result from(MajorQuestion domain) {
            return new Result(
                    domain.getId(),
                    domain.getContent(),
                    WeightVo.from(domain.getWeightVo()),
                    domain.getCreatedAt()
            );
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class WeightVo {
            private final Integer web;
            private final Integer app;
            private final Integer server;
            private final Integer ai;
            private final Integer game;

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
