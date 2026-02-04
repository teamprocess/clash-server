package com.process.clash.adapter.web.roadmap.v2.chapter.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.chapter.data.UpdateChapterV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateChapterV2Dto {

    @Schema(name = "UpdateChapterV2DtoRequest")
    public record Request(
            @NotBlank(message = "title은 필수 입력값입니다.")
            String title,

            String description,

            @NotNull(message = "orderIndex는 필수 입력값입니다.")
            Integer orderIndex,

            String studyMaterialUrl
    ) {
        public UpdateChapterV2Data.Command toCommand(Actor actor, Long chapterId) {
            return new UpdateChapterV2Data.Command(actor, chapterId, title, description, orderIndex, studyMaterialUrl);
        }
    }

    @Schema(name = "UpdateChapterV2DtoResponse")
    public record Response(
            Long chapterId,
            String title,
            String description,
            Integer orderIndex,
            String studyMaterialUrl
    ) {
        public static Response from(UpdateChapterV2Data.Result result) {
            return new Response(
                    result.chapterId(),
                    result.title(),
                    result.description(),
                    result.orderIndex(),
                    result.studyMaterialUrl()
            );
        }
    }
}
