package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.UpdateCategoryData;
import jakarta.validation.constraints.NotBlank;

public class UpdateCategoryDto {

    public record Request(
            @NotBlank(message = "name은 필수 입력값입니다.")
            String name
    ) {
        public UpdateCategoryData.Command toCommand(Actor actor, Long categoryId) {
            return new UpdateCategoryData.Command(actor, categoryId, name);
        }
    }

    public record Response(Long categoryId, String name, String updatedAt) {
        public static Response from(UpdateCategoryData.Result result) {
            return new Response(result.categoryId(), result.name(), result.updatedAt());
        }
    }
}