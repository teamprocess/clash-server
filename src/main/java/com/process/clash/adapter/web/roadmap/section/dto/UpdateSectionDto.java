package com.process.clash.adapter.web.roadmap.section.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateSectionDto {

    public record Request(
            @Size(min = 1, message = "title이 제공되는 경우 비워둘 수 없습니다.")
            String title,        // 선택사항

            @Size(min = 1, message = "category가 제공되는 경우 비워둘 수 없습니다.")
            Long categoryId,     // 선택사항

            @Size(min = 1, message = "description이 제공되는 경우 비워둘 수 없습니다.")
            String description,  // 선택사항

            @Min(value = 0, message = "orderIndex는 0 이상이어야 합니다.")
            Integer orderIndex,  // 선택사항

            @Size(min = 1, message = "keyPoints가 제공되는 경우 비워둘 수 없습니다.")
            List<String> keyPoints,  // 선택사항

            List<Long> prerequisiteSectionIds  // 선택사항: 선행 로드맵 ID 목록
    ) {
        public UpdateSectionData.Command toCommand(Actor actor, Long sectionId) {
            return new UpdateSectionData.Command(actor, sectionId, title, categoryId, description, orderIndex, keyPoints, prerequisiteSectionIds);
        }
    }

    public record Response(
            Long sectionId,
            String title,
            String category,
            String description,
            List<String> keyPoints,
            String updatedAt
    ) {
        public static Response from(UpdateSectionData.Result result) {
            return new Response(
                    result.sectionId(),
                    result.title(),
                    result.category(),
                    result.description(),
                    result.keyPoints(),
                    result.updatedAt()
            );
        }
    }
}
