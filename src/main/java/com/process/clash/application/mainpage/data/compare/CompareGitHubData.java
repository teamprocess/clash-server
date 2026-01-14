package com.process.clash.application.mainpage.data.compare;

import com.process.clash.application.common.actor.Actor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CompareGitHubData {

    public record Command(
            Actor actor
    ) {

        public static Command from(Actor actor) {

            return new Command(
                    actor
            );
        }
    }

    public record Result(
            OneDayStats yesterday,
            OneDayStats today
    ) {

//        public static Result from() {
//
//        }
    }

    public record OneDayStats(
            LocalDate date,
            Commit commit,
            PullRequest pullRequest,
            Issue issue,
            Review review
    ) {}

    public record Commit(
            Integer count,
            String representationRepo,
            Integer addLines,
            Integer removeLines,
            LocalDateTime firstCommit,
            LocalDateTime lastCommit
    ) {}

    public record PullRequest(
            Integer count,
            String representationRepo,
            Integer mergedCount,
            Integer openCount,
            Integer closedCount,
            Integer inReviewCount,
            Integer approvedCount,
            Integer requestCount
    ) {}

    public record Issue(
            Integer count
    ) {}

    public record Review(
            Integer count
    ) {}
}