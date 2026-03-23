package com.process.clash.adapter.web.roadmap.v2.section.dto;

import com.process.clash.application.roadmap.v2.section.data.GetSectionV2DetailsData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GetSectionV2DetailsDto {

    @Schema(name = "GetSectionV2DetailsDtoResponse")
    public record Response(
            Long sectionId,
            String sectionTitle,
            Boolean completed,
            Integer totalChapters,
            Long currentChapterId,
            Integer currentOrderIndex,
            List<ChapterVo> chapters
    ) {
        public static Response from(GetSectionV2DetailsData.Result result) {
            return new Response(
                    result.sectionId(),
                    result.sectionTitle(),
                    result.completed(),
                    result.totalChapters(),
                    result.currentChapterId(),
                    result.currentOrderIndex(),
                    result.chapters().stream().map(ChapterVo::from).toList()
            );
        }
    }

    public record ChapterVo(
            Long chapterId,
            String title,
            String description,
            Integer orderIndex,
            String studyMaterialUrl
    ) {
        public static ChapterVo from(GetSectionV2DetailsData.Result.ChapterVo vo) {
            return new ChapterVo(vo.chapterId(), vo.title(), vo.description(), vo.orderIndex(), vo.studyMaterialUrl());
        }
    }
}
