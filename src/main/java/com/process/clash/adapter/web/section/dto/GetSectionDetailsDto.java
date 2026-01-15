package com.process.clash.adapter.web.section.dto;

import java.util.List;

public class GetSectionDetailsDto {

    public record Response(
            Long sectionId,
            String sectionTitle,
            Integer totalChapters,
            Integer currentChapters,
            List<ChapterVo> chapters
    ) {
        // TODO: Implement from() method when Result is available
        // public static Response from(GetSectionDetailsData.Result result) { ... }
    }

    public record ChapterVo(
            Long id,
            String title,
            Integer difficulty,
            List<MissionVo> missions
    ) {
        // TODO: Implement from() method when Result.ChapterVo is available
        // public static ChapterVo from(GetSectionDetailsData.Result.ChapterVo vo) { ... }
    }

    public record MissionVo(
            Long id,
            String title,
            List<QuestionVo> questions
    ) {
        // TODO: Implement from() method when Result.MissionVo is available
        // public static MissionVo from(GetSectionDetailsData.Result.MissionVo vo) { ... }
    }

    public record QuestionVo(
            Long id,
            String title,
            List<ChoiceVo> choices
    ) {
        // TODO: Implement from() method when Result.QuestionVo is available
        // public static QuestionVo from(GetSectionDetailsData.Result.QuestionVo vo) { ... }
    }

    public record ChoiceVo(
            Long id,
            String content
    ) {
        // TODO: Implement from() method when Result.ChoiceVo is available
        // public static ChoiceVo from(GetSectionDetailsData.Result.ChoiceVo vo) { ... }
    }
}
