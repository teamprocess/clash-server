package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.CreateCategoryData;
import jakarta.validation.constraints.NotBlank;

public class CreateCategoryDto {

    public record Request(
            @NotBlank(message = "name은 필수 입력값입니다.")
            String name
    ) {
        public CreateCategoryData.Command toCommand(Actor actor) {
            return new CreateCategoryData.Command(actor, name);
        }
    }

    public record Response(Long categoryId, String name, String createdAt) {
        public static Response from(CreateCategoryData.Result result) {
            return new Response(result.categoryId(), result.name(), result.createdAt());
        }
    }
}