package com.process.clash.adapter.persistence.github;

import com.process.clash.domain.github.entity.GitHubDailyStats;
import org.springframework.stereotype.Component;

@Component
public class GitHubDailyStatsJpaMapper {

    public GitHubDailyStatsJpaEntity toJpaEntity(GitHubDailyStats stat) {
        return new GitHubDailyStatsJpaEntity(
                stat.userId(),
                stat.studyDate(),
                stat.commitCount(),
                stat.prCount(),
                stat.issueCount(),
                stat.reviewedPrCount(),
                stat.additions(),
                stat.deletions(),
                stat.topCommitRepo(),
                stat.topPrRepo(),
                stat.firstCommitAt(),
                stat.lastCommitAt(),
                stat.prMergedCount(),
                stat.prOpenCount(),
                stat.prClosedCount(),
                stat.syncedAt()
        );
    }

    public void updateEntity(GitHubDailyStatsJpaEntity entity, GitHubDailyStats stat) {
        entity.updateStats(
                stat.commitCount(),
                stat.prCount(),
                stat.issueCount(),
                stat.reviewedPrCount(),
                stat.additions(),
                stat.deletions(),
                stat.topCommitRepo(),
                stat.topPrRepo(),
                stat.firstCommitAt(),
                stat.lastCommitAt(),
                stat.prMergedCount(),
                stat.prOpenCount(),
                stat.prClosedCount(),
                stat.syncedAt()
        );
    }

    public GitHubDailyStats toDomain(GitHubDailyStatsJpaEntity entity) {
        return new GitHubDailyStats(
                entity.getUserId(),
                entity.getStudyDate(),
                entity.getCommitCount(),
                entity.getPrCount(),
                entity.getIssueCount(),
                entity.getReviewCount(),
                entity.getAdditions(),
                entity.getDeletions(),
                entity.getTopCommitRepo(),
                entity.getTopPrRepo(),
                entity.getFirstCommitAt(),
                entity.getLastCommitAt(),
                entity.getPrMergedCount(),
                entity.getPrOpenCount(),
                entity.getPrClosedCount(),
                entity.getSyncedAt()
        );
    }
}
