package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.PostMajorQuestionData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostMajorQuestionDto {

    public record Request(
            @NotBlank(message = "질문 내용은 비워둘 수 없습니다.")
            String content,
            @NotNull(message = "가중치는 비워둘 수 없습니다.")
            @Valid
            MajorWeightVo weight
    ) {
        public PostMajorQuestionData.Command toCommand(Actor actor) {
            return new PostMajorQuestionData.Command(
                    actor,
                    content,
                    new PostMajorQuestionData.Command.WeightVo(
                            weight.getWeb(),
                            weight.getApp(),
                            weight.getServer(),
                            weight.getAi(),
                            weight.getGame()
                    )
            );
        }
    }

    public record Response(
            Long questionId,
            String content,
            MajorWeightVo weight,
            LocalDateTime createdAt
    ) {
        public static Response from(PostMajorQuestionData.Result result) {
            return new Response(
                    result.getQuestionId(),
                    result.getContent(),
                    new MajorWeightVo(
                            result.getWeight().getWeb(),
                            result.getWeight().getApp(),
                            result.getWeight().getServer(),
                            result.getWeight().getAi(),
                            result.getWeight().getGame()
                    ),
                    result.getCreatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MajorWeightVo {

        @NotNull(message = "web 가중치는 비워둘 수 없습니다.")
        private final Integer web;

        @NotNull(message = "app 가중치는 비워둘 수 없습니다.")
        private final Integer app;

        @NotNull(message = "server 가중치는 비워둘 수 없습니다.")
        private final Integer server;

        @NotNull(message = "ai 가중치는 비워둘 수 없습니다.")
        private final Integer ai;

        @NotNull(message = "game 가중치는 비워둘 수 없습니다.")
        private final Integer game;
    }
}
