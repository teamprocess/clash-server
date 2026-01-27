package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetMyGitHubActivityData;
import java.util.List;

public class GetMyGitHubActivityDto {

    public record Response(
        int totalContributions,
        List<Contribution> contributions
    ) {
        public static Response from(GetMyGitHubActivityData.Result result) {
            List<Contribution> contributions = result.contributions().stream()
                    .map(Contribution::from)
                    .toList();
            return new Response(result.totalContributions(), contributions);
        }
    }

    public record Contribution(
        String date,
        int contributionCount,
        int contributionLevel
    ) {
        public static Contribution from(GetMyGitHubActivityData.Contribution contribution) {
            return new Contribution(
                    contribution.date(),
                    contribution.contributionCount(),
                    contribution.contributionLevel()
            );
        }
    }
}
