package com.process.clash.adapter.web.mainpage.dto.compare;

import com.process.clash.application.mainpage.data.compare.CompareGitHubData;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CompareGitHubDto {

    public record Response(
            OneDayStats yesterday,
            OneDayStats today
    ) {
        public static Response from(CompareGitHubData.Result result) {
            return new Response(
                    OneDayStats.from(result.yesterday()),
                    OneDayStats.from(result.today())
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
        public static OneDayStats from(CompareGitHubData.OneDayStats data) {
            return new OneDayStats(
                    data.date(),
                    Commit.from(data.commit()),
                    PullRequest.from(data.pullRequest()),
                    Issue.from(data.issue()),
                    Review.from(data.review())
            );
        }
    }

    public record Commit(
            Integer count,
            String representationRepo,
            Integer addLines,
            Integer removeLines,
            LocalDateTime firstCommit,
            LocalDateTime lastCommit
    ) {
        public static Commit from(CompareGitHubData.Commit data) {
            return new Commit(
                    data.count(),
                    data.representationRepo(),
                    data.addLines(),
                    data.removeLines(),
                    data.firstCommit(),
                    data.lastCommit()
            );
        }
    }

    public record PullRequest(
            Integer count,
            String representationRepo,
            Integer mergedCount,
            Integer openCount,
            Integer closedCount,
            Integer inReviewCount,
            Integer approvedCount,
            Integer requestCount
    ) {
        public static PullRequest from(CompareGitHubData.PullRequest data) {
            return new PullRequest(
                    data.count(),
                    data.representationRepo(),
                    data.mergedCount(),
                    data.openCount(),
                    data.closedCount(),
                    data.inReviewCount(),
                    data.approvedCount(),
                    data.requestCount()
            );
        }
    }

    public record Issue(
            Integer count
    ) {
        public static Issue from(CompareGitHubData.Issue data) {
            return new Issue(data.count());
        }
    }

    public record Review(
            Integer count
    ) {
        public static Review from(CompareGitHubData.Review data) {
            return new Review(data.count());
        }
    }
}