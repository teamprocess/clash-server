package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetMyGitHubActivityDetailData;

public class GetMyGitHubActivityDetailDto {

    public record Response(
        String date,
        int contributionCount,
        int contributionLevel,
        int commitsCount,
        int issuesCount,
        int prCount,
        int reviewsCount,
        long additionLines,
        long deletionLines,
        String topCommitRepo
    ) {
        public static Response from(GetMyGitHubActivityDetailData.Result result) {
            return new Response(
                    result.date(),
                    result.contributionCount(),
                    result.contributionLevel(),
                    result.commitsCount(),
                    result.issuesCount(),
                    result.prCount(),
                    result.reviewsCount(),
                    result.additionLines(),
                    result.deletionLines(),
                    result.topCommitRepo()
            );
        }
    }
}
