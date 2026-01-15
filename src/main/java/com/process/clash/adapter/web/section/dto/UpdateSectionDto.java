package com.process.clash.adapter.web.section.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateSectionDto {

    public record Request(
            @Size(min = 1, message = "title이 제공되는 경우 비워둘 수 없습니다.")
            String title,        // 선택사항

            @Size(min = 1, message = "category가 제공되는 경우 비워둘 수 없습니다.")
            String category,     // 선택사항

            @Size(min = 1, message = "description이 제공되는 경우 비워둘 수 없습니다.")
            String description,  // 선택사항

            @Size(min = 1, message = "keyPoints가 제공되는 경우 비워둘 수 없습니다.")
            List<String> keyPoints  // optional
    ) {
        // TODO: Command 사용 가능 시 toCommand() 메서드 구현 필요
        // public UpdateSectionData.Command toCommand(Actor actor, Long sectionId) { ... }
    }

    public record Response(
            Long sectionId,
            String title,
            String category,
            String description,
            List<String> keyPoints,
            String updatedAt
    ) {
        // TODO: Result 사용 가능 시 from() 메서드 구현 필요
        // public static Response from(UpdateSectionData.Result result) { ... }
    }
}
