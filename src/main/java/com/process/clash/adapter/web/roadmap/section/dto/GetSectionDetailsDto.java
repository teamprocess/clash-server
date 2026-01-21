package com.process.clash.adapter.web.roadmap.section.dto;

import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetSectionDetailsDto {

    @Schema(name = "GetSectionDetailsDtoResponse")

    public record Response(
            @Schema(description = "로드맵 ID", example = "1")
            Long sectionId,
            @Schema(description = "로드맵 제목", example = "스프링 입문")
            String sectionTitle,
            @Schema(description = "총 챕터 수", example = "3")
            Integer totalChapters,
            @Schema(description = "현재 챕터 ID", example = "1")
            Long currentChapterId,
            @Schema(description = "현재 챕터 순서 (0-based)", example = "0")
            Integer currentOrderIndex,
            @Schema(description = "현재 미션 순서 (0-based)", example = "2")
            Integer currentMissionIndex,
            List<ChapterVo> chapters
    ) {
        public static Response from(GetSectionDetailsData.Result result) {
            List<ChapterVo> chapters = result.chapters().stream()
                    .map(ChapterVo::from)
                    .toList();
            return new Response(
                    result.sectionId(),
                    result.sectionTitle(),
                    result.totalChapters(),
                    result.currentChapterId(),
                    result.currentOrderIndex(),
                    result.currentMissionIndex(),
                    chapters
            );
        }
    }

    public record ChapterVo(
            Long id,
            String title,
            Integer orderIndex,
            Integer completedMissions,
            Integer totalMissions
    ) {
        public static ChapterVo from(GetSectionDetailsData.Result.ChapterVo vo) {
            return new ChapterVo(vo.id(), vo.title(), vo.orderIndex(), vo.completedMissions(), vo.totalMissions());
        }
    }
}
