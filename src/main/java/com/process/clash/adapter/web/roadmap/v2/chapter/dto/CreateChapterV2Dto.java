package com.process.clash.adapter.web.roadmap.v2.chapter.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.chapter.data.CreateChapterV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateChapterV2Dto {

    @Schema(name = "CreateChapterV2DtoRequest")
    public record Request(
            @NotNull(message = "sectionId는 필수 입력값입니다.")
            Long sectionId,

            @NotBlank(message = "title은 필수 입력값입니다.")
            String title,

            String description,

            @NotNull(message = "orderIndex는 필수 입력값입니다.")
            Integer orderIndex,

            String studyMaterialUrl
    ) {
        public CreateChapterV2Data.Command toCommand(Actor actor) {
            return new CreateChapterV2Data.Command(actor, sectionId, title, description, orderIndex, studyMaterialUrl);
        }
    }

    @Schema(name = "CreateChapterV2DtoResponse")
    public record Response(
            Long chapterId,
            Long sectionId,
            String title,
            String description,
            Integer orderIndex,
            String studyMaterialUrl
    ) {
        public static Response from(CreateChapterV2Data.Result result) {
            return new Response(
                    result.chapterId(),
                    result.sectionId(),
                    result.title(),
                    result.description(),
                    result.orderIndex(),
                    result.studyMaterialUrl()
            );
        }
    }
}
