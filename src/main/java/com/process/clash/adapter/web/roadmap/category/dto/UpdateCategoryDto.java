package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.UpdateCategoryData;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public class UpdateCategoryDto {

    @Schema(name = "UpdateCategoryDtoRequest")

    public record Request(
            @NotBlank(message = "name은 필수 입력값입니다.")
            String name
    ) {
        public UpdateCategoryData.Command toCommand(Actor actor, Long categoryId) {
            return new UpdateCategoryData.Command(actor, categoryId, name);
        }
    }

    @Schema(name = "UpdateCategoryDtoResponse")

    public record Response(Long categoryId, String name, Instant updatedAt) {
        public static Response from(UpdateCategoryData.Result result) {
            return new Response(result.categoryId(), result.name(), result.updatedAt());
        }
    }
}
