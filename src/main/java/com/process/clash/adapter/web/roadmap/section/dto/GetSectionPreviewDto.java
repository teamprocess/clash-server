package com.process.clash.adapter.web.roadmap.section.dto;

import com.process.clash.application.roadmap.section.data.GetSectionPreviewData;

import java.util.List;

public class GetSectionPreviewDto {

    public record Response(
            Long id,
            String title,
            String description,
            Long totalChapters,
            List<ChapterVo> chapters,
            List<String> keyPoints
    ) {
        public static Response from(GetSectionPreviewData.Result result) {
            List<ChapterVo> chapters = result.chapters().stream()
                    .map(ChapterVo::from)
                    .toList();
            return new Response(
                    result.id(),
                    result.title(),
                    result.description(),
                    result.totalChapters(),
                    chapters,
                    result.keyPoints()
            );
        }
    }

    public record ChapterVo(
            Long id,
            String title,
            String description
    ) {
        public static ChapterVo from(GetSectionPreviewData.Result.ChapterVo vo) {
            return new ChapterVo(vo.id(), vo.title(), vo.description());
        }
    }
}
