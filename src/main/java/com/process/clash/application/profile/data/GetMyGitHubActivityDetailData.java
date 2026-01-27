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
        int contributionsCount,
        int contributionsLevel,
        int commitsCount,
        int issuesCount,
        int prCount,
        int reviewsCount,
        long additionLines,
        long deletionLines
    ) {
        public static Result of(
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
            return new Result(
                date,
                contributionsCount,
                contributionsLevel,
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
