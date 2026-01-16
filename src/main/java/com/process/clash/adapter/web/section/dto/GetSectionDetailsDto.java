package com.process.clash.adapter.web.section.dto;

import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;

import java.util.List;

public class GetSectionDetailsDto {

    public record Response(
            Long sectionId,
            String sectionTitle,
            Integer totalChapters,
            Long currentChapterId,
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
                    chapters
            );
        }
    }

    public record ChapterVo(
            Long id,
            String title,
            Integer difficulty,
            List<MissionVo> missions
    ) {
        public static ChapterVo from(GetSectionDetailsData.Result.ChapterVo vo) {
            List<MissionVo> missions = vo.missions().stream()
                    .map(MissionVo::from)
                    .toList();
            return new ChapterVo(vo.id(), vo.title(), vo.difficulty(), missions);
        }
    }

    public record MissionVo(
            Long id,
            String title,
            List<QuestionVo> questions
    ) {
        public static MissionVo from(GetSectionDetailsData.Result.MissionVo vo) {
            List<QuestionVo> questions = vo.questions().stream()
                    .map(QuestionVo::from)
                    .toList();
            return new MissionVo(vo.id(), vo.title(), questions);
        }
    }

    public record QuestionVo(
            Long id,
            String title,
            List<ChoiceVo> choices
    ) {
        public static QuestionVo from(GetSectionDetailsData.Result.QuestionVo vo) {
            List<ChoiceVo> choices = vo.choices().stream()
                    .map(ChoiceVo::from)
                    .toList();
            return new QuestionVo(vo.id(), vo.title(), choices);
        }
    }

    public record ChoiceVo(
            Long id,
            String content
    ) {
        public static ChoiceVo from(GetSectionDetailsData.Result.ChoiceVo vo) {
            return new ChoiceVo(vo.id(), vo.content());
        }
    }
}
