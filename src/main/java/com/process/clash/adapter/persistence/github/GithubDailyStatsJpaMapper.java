package com.process.clash.adapter.persistence.github;

import com.process.clash.domain.github.entity.GithubDailyStats;
import org.springframework.stereotype.Component;

@Component
public class GithubDailyStatsJpaMapper {

    public GithubDailyStatsJpaEntity toJpaEntity(GithubDailyStats stat) {
        return new GithubDailyStatsJpaEntity(
                stat.id(),
                stat.userId(),
                stat.studyDate(),
                stat.commitCount(),
                stat.prCount(),
                stat.issueCount(),
                stat.reviewedPrCount(),
                stat.additions(),
                stat.deletions(),
                stat.syncedAt()
        );
    }

    public GithubDailyStats toDomain(GithubDailyStatsJpaEntity entity) {
        return new GithubDailyStats(
                entity.getId(),
                entity.getUserId(),
                entity.getStudyDate(),
                entity.getCommitCount(),
                entity.getPrCount(),
                entity.getIssueCount(),
                entity.getReviewCount(),
                entity.getAdditions(),
                entity.getDeletions(),
                entity.getSyncedAt()
        );
    }
}
