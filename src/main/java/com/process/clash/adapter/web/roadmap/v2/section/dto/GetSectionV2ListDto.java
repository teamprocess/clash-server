package com.process.clash.adapter.web.roadmap.v2.section.dto;

import com.process.clash.application.roadmap.v2.section.data.GetSectionV2ListData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GetSectionV2ListDto {

    @Schema(name = "GetSectionV2ListDtoResponse")
    public record Response(
            List<SectionVo> sections,
            List<Long> categories,
            Integer completedSections,
            Integer totalSections
    ) {
        public static Response from(GetSectionV2ListData.Result result) {
            return new Response(
                    result.sections().stream().map(SectionVo::from).toList(),
                    result.categories(),
                    result.completedSections(),
                    result.totalSections()
            );
        }
    }

    public record SectionVo(
            Long id,
            String title,
            Long categoryId,
            String categoryImageUrl,
            Boolean completed,
            Boolean locked,
            List<ChapterVo> chapters
    ) {
        public static SectionVo from(GetSectionV2ListData.Result.SectionVo vo) {
            return new SectionVo(
                    vo.id(),
                    vo.title(),
                    vo.categoryId(),
                    vo.categoryImageUrl(),
                    vo.completed(),
                    vo.locked(),
                    vo.chapters().stream().map(ChapterVo::from).toList()
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
        public static ChapterVo from(GetSectionV2ListData.Result.ChapterVo vo) {
            return new ChapterVo(
                    vo.chapterId(),
                    vo.title(),
                    vo.description(),
                    vo.orderIndex(),
                    vo.studyMaterialUrl()
            );
        }
    }
}
