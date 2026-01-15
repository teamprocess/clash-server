package com.process.clash.adapter.web.section.dto;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class UpdateSectionDto {

    public record Request(
            String title,        // optional
            String category,     // optional
            String description,  // optional
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
