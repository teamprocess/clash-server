package com.process.clash.adapter.web.roadmap.missions.dto;

import com.process.clash.application.roadmap.missions.data.GetMissionResultData;

public class MissionResultDto {

    public record Response(
            Long missionId,
            Boolean isCleared,
            Integer correctCount,
            Integer totalCount,
            Long nextMissionId,
            Integer nextMissionOrderIndex,
            Long nextChapterId,
            Integer nextChapterOrderIndex
    ) {
        public static Response fromResult(GetMissionResultData.Result result) {
            return new Response(
                    result.missionId(),
                    result.isCleared(),
                    result.correctCount(),
                    result.totalCount(),
                    result.nextMissionId(),
                    result.nextMissionOrderIndex(),
                    result.nextChapterId(),
                    result.nextChapterOrderIndex()
            );
        }
    }
}