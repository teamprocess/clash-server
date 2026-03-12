package com.process.clash.adapter.web.roadmap.v2.section.dto;

import com.process.clash.application.roadmap.v2.section.data.GetSectionV2PreviewData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GetSectionV2PreviewDto {

    @Schema(name = "GetSectionV2PreviewDtoResponse")
    public record Response(
            Long id,
            String title,
            String description,
            Long totalChapters,
            List<ChapterVo> chapters,
            List<String> keyPoints
    ) {
        public static Response from(GetSectionV2PreviewData.Result result) {
            return new Response(
                    result.id(),
                    result.title(),
                    result.description(),
                    result.totalChapters(),
                    result.chapters().stream().map(ChapterVo::from).toList(),
                    result.keyPoints()
            );
        }
    }

    public record ChapterVo(
            Long id,
            String title,
            String description
    ) {
        public static ChapterVo from(GetSectionV2PreviewData.Result.ChapterVo vo) {
            return new ChapterVo(vo.id(), vo.title(), vo.description());
        }
    }
}