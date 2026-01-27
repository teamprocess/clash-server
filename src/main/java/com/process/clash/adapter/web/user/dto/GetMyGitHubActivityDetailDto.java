package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetMyGitHubActivityDetailData;

public class GetMyGitHubActivityDetailDto {

    public record Response(
        String date,
        int contributionsCount,
        int contributionsLevel,
        int commitsCount,
        int issuesCount,
        int prCount,
        int reviewsCount,
        long additionLines,
        long deletionLines
    ) {
        public static Response from(GetMyGitHubActivityDetailData.Result result) {
            return new Response(
                    result.date(),
                    result.contributionsCount(),
                    result.contributionsLevel(),
                    result.commitsCount(),
                    result.issuesCount(),
                    result.prCount(),
                    result.reviewsCount(),
                    result.additionLines(),
                    result.deletionLines()
            );
        }
    }
}
