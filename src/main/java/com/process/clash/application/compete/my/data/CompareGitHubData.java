package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.github.entity.GitHubDailyStats;

import java.time.Instant;
import java.time.LocalDate;

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

        public static Result of(GitHubDailyStats yesterday, GitHubDailyStats today) {

            return new Result(
                    OneDayStats.from(yesterday),
                    OneDayStats.from(today)
            );
        }
    }

    public record OneDayStats(
            LocalDate date,
            Commit commit,
            PullRequest pullRequest,
            Issue issue,
            Review review
    ) {

        public static OneDayStats from(GitHubDailyStats gitHubDailyStats) {

            return new OneDayStats(
                    gitHubDailyStats.studyDate(),
                    Commit.from(gitHubDailyStats),
                    PullRequest.from(gitHubDailyStats),
                    Issue.from(gitHubDailyStats),
                    Review.from(gitHubDailyStats)
            );
        }
    }

    public record Commit(
            Integer count,
            String representationRepo,
            Long addLines,
            Long removeLines,
            Instant firstCommit,
            Instant lastCommit
    ) {

        public static Commit from(GitHubDailyStats gitHubDailyStats) {

            return new Commit(
                    gitHubDailyStats.commitCount(),
                    gitHubDailyStats.topCommitRepo(),
                    gitHubDailyStats.additions(),
                    gitHubDailyStats.deletions(),
                    gitHubDailyStats.firstCommitAt(),
                    gitHubDailyStats.lastCommitAt()
            );
        }
    }

    public record PullRequest(
            Integer count,
            String representationRepo,
            Integer mergedCount,
            Integer openCount,
            Integer closedCount
    ) {

        public static PullRequest from(GitHubDailyStats gitHubDailyStats) {

            return new PullRequest(
                    gitHubDailyStats.prCount(),
                    gitHubDailyStats.topPrRepo(),
                    gitHubDailyStats.prMergedCount(),
                    gitHubDailyStats.prOpenCount(),
                    gitHubDailyStats.prClosedCount()
            );
        }
    }

    public record Issue(
            Integer count
    ) {

        public static Issue from(GitHubDailyStats gitHubDailyStats) {

            return new Issue(
                    gitHubDailyStats.issueCount()
            );
        }
    }

    public record Review(
            Integer count
    ) {

        public static Review from(GitHubDailyStats gitHubDailyStats) {

            return new Review(
                    gitHubDailyStats.reviewedPrCount()
            );
        }
    }
}