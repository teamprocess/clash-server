package com.process.clash.adapter.web.roadmap.section.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.section.data.CreateSectionData;
import com.process.clash.domain.common.enums.Major;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateSectionDto {

    @Schema(name = "CreateSectionDtoRequest")

    public record Request(
            @NotNull(message = "major는 필수 입력값입니다.")
            Major major,

            @NotBlank(message = "title은 필수 입력값입니다.")
            String title,

            @NotNull(message = "categoryId는 필수 입력값입니다.")
            Long categoryId,

            @NotBlank(message = "description은 필수 입력값입니다.")
            String description,

            @NotEmpty(message = "keyPoints는 비어있을 수 없습니다.")
            List<String> keyPoints
    ) {
        public CreateSectionData.Command toCommand(Actor actor) {
            return new CreateSectionData.Command(actor, major, title, categoryId, description, keyPoints);
        }
    }

    @Schema(name = "CreateSectionDtoResponse")

    public record Response(
            Long sectionId,
            String major,
            String title,
            String category,
            String description,
            List<String> keyPoints,
            String createdAt
    ) {
        public static Response from(CreateSectionData.Result result) {
            return new Response(
                    result.sectionId(),
                    result.major(),
                    result.title(),
                    result.category(),
                    result.description(),
                    result.keyPoints(),
                    result.createdAt()
            );
        }
    }
}
