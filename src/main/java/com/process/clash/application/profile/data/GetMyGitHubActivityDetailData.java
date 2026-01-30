package com.process.clash.application.profile.data;

import com.process.clash.application.common.actor.Actor;
import java.time.LocalDate;

public class GetMyGitHubActivityDetailData {

    public record Command(
        Actor actor,
        LocalDate date
    ) {}

    public record Result(
        String date,
        int contributionCount,
        int contributionLevel,
        int commitsCount,
        int issuesCount,
        int prCount,
        int reviewsCount,
        long additionLines,
        long deletionLines
    ) {
        public static Result of(
            String date,
            int contributionCount,
            int contributionLevel,
            int commitsCount,
            int issuesCount,
            int prCount,
            int reviewsCount,
            long additionLines,
            long deletionLines
        ) {
            return new Result(
                date,
                contributionCount,
                contributionLevel,
                commitsCount,
                issuesCount,
                prCount,
                reviewsCount,
                additionLines,
                deletionLines
            );
        }
    }
}
