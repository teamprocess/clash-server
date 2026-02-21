package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.UpdateCategoryImageData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateCategoryImageDto {

    @Schema(name = "UpdateCategoryImageDtoRequest")
    public record Request(
            @NotBlank(message = "imageUrl은 필수 입력값입니다.")
            @Pattern(
                    regexp = "^https?://.+$",
                    message = "imageUrl은 http(s) URL 형식이어야 합니다."
            )
            String imageUrl
    ) {
        public UpdateCategoryImageData.Command toCommand(Actor actor, Long categoryId) {
            return new UpdateCategoryImageData.Command(actor, categoryId, imageUrl);
        }
    }

    @Schema(name = "UpdateCategoryImageDtoResponse")
    public record Response(Long categoryId, String imageUrl) {
        public static Response from(UpdateCategoryImageData.Result result) {
            return new Response(result.categoryId(), result.imageUrl());
        }
    }
}
