package com.process.clash.adapter.web.roadmap.chapter.dto;

import com.process.clash.application.roadmap.chapter.data.GetChapterDetailsData;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetChapterDetailsDto {

    @Schema(name = "GetChapterDetailsDtoResponse")

    public record Response(
            Long chapterId,
            String title,
            String description,
            Long currentMissionId,
            Long currentQuestionId,
            Integer currentQuestionIndex,
            Integer currentQuestionNumber,
            Integer totalQuestions,
            List<MissionVo> missions
    ) {
        public static Response from(GetChapterDetailsData.Result result) {
            List<MissionVo> missions = result.missions().stream()
                    .map(MissionVo::from)
                    .toList();
            return new Response(
                    result.chapterId(),
                    result.title(),
                    result.description(),
                    result.currentMissionId(),
                    result.currentQuestionId(),
                    result.currentQuestionIndex(),
                    result.currentQuestionNumber(),
                    result.totalQuestions(),
                    missions
            );
        }
    }

    public record MissionVo(
            Long id,
            String title,
            List<QuestionVo> questions
    ) {
        public static MissionVo from(GetChapterDetailsData.Result.MissionVo vo) {
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
        public static QuestionVo from(GetChapterDetailsData.Result.QuestionVo vo) {
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
        public static ChoiceVo from(GetChapterDetailsData.Result.ChoiceVo vo) {
            return new ChoiceVo(vo.id(), vo.content());
        }
    }
}
