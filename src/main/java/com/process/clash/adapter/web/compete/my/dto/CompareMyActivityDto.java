package com.process.clash.adapter.web.compete.my.dto;


import com.process.clash.application.compete.my.data.CompareMyActivityData;

public class CompareMyActivityDto {

    public record Response(
            Double earnedExp,
            Double studyTime,
            Double gitHubAttribution
    ) {

        public static Response from(CompareMyActivityData.Result result) {

            return new Response(
                    result.earnedExp(),
                    result.studyTime(),
                    result.gitHubAttribution()
            );
        }
    }
}
