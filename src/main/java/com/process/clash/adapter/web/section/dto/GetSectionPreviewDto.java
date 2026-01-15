package com.process.clash.adapter.web.section.dto;

import java.util.List;

public class GetSectionPreviewDto {

    public record Response(
            Long id,
            String title,
            String description,
            Long totalSteps,
            List<ChapterVo> chapters,
            List<String> keyPoints
    ) {
        // TODO: Implement from() method when Result is available
        // public static Response from(GetSectionPreviewData.Result result) { ... }
    }

    public record ChapterVo(
            Long id,
            String title,
            String description
    ) {
        // TODO: Implement from() method when Result.ChapterVo is available
        // public static ChapterVo from(GetSectionPreviewData.Result.ChapterVo vo) { ... }
    }
}
