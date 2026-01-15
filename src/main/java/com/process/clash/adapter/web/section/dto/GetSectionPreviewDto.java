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
        // TODO: Result 사용 가능 시 from() 메서드 구현 필요
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
