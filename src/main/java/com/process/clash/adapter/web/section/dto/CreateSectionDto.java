package com.process.clash.adapter.web.section.dto;

import com.process.clash.domain.common.enums.Major;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateSectionDto {

    public record Request(
            @NotNull(message = "major는 필수 입력값입니다.")
            Major major,

            @NotBlank(message = "title은 필수 입력값입니다.")
            String title,

            @NotBlank(message = "category는 필수 입력값입니다.")
            String category,

            @NotBlank(message = "description은 필수 입력값입니다.")
            String description,

            @NotEmpty(message = "keyPoints는 비어있을 수 없습니다.")
            List<String> keyPoints
    ) {
        // TODO: Implement toCommand() method when Command is available
        // public CreateSectionData.Command toCommand(Actor actor) { ... }
    }

    public record Response(
            Long sectionId,
            String major,
            String title,
            String category,
            String description,
            List<String> keyPoints,
            String createdAt
    ) {
        // TODO: Implement from() method when Result is available
        // public static Response from(CreateSectionData.Result result) { ... }
    }
}
