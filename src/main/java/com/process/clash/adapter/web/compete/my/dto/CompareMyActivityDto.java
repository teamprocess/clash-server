package com.process.clash.adapter.web.compete.my.dto;


import com.process.clash.application.compete.my.data.CompareMyActivityData;
import io.swagger.v3.oas.annotations.media.Schema;

public class CompareMyActivityDto {

    @Schema(name = "CompareMyActivityDtoResponse")
    public record Response(
            Double earnedExp,
            Double studyTime,
            Double gitHubAttribution,
            Double commitCount
    ) {

        public static Response from(CompareMyActivityData.Result result) {

            return new Response(
                    result.earnedExp(),
                    result.studyTime(),
                    result.gitHubAttribution(),
                    result.commitCount()
            );
        }
    }
}