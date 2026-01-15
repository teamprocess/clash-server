package com.process.clash.adapter.web.section.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateSectionDto {

    public record Request(
            @Size(min = 1, message = "title이 제공되는 경우 비워둘 수 없습니다.")
            String title,        // optional

            @Size(min = 1, message = "category가 제공되는 경우 비워둘 수 없습니다.")
            String category,     // optional

            @Size(min = 1, message = "description이 제공되는 경우 비워둘 수 없습니다.")
            String description,  // optional

            @Size(min = 1, message = "keyPoints가 제공되는 경우 비워둘 수 없습니다.")
            List<String> keyPoints  // optional
    ) {
        // TODO: Implement toCommand() method when Command is available
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
        // TODO: Implement from() method when Result is available
        // public static Response from(UpdateSectionData.Result result) { ... }
    }
}
