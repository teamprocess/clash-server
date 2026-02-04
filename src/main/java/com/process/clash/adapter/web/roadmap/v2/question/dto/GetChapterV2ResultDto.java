package com.process.clash.adapter.web.roadmap.v2.question.dto;

import com.process.clash.application.roadmap.v2.question.data.GetChapterV2ResultData;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetChapterV2ResultDto {

    @Schema(name = "GetChapterV2ResultDtoResponse")
    public record Response(
            boolean isCleared,
            Integer correctCount,
            Integer totalCount,
            Integer scorePercentage
    ) {
        public static Response from(GetChapterV2ResultData.Result result) {
            return new Response(
                    result.isCleared(),
                    result.correctCount(),
                    result.totalCount(),
                    result.scorePercentage()
            );
        }
    }
}
