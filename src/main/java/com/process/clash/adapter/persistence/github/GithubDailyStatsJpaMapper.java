package com.process.clash.adapter.persistence.github;

import com.process.clash.domain.github.entity.GithubDailyStats;
import org.springframework.stereotype.Component;

@Component
public class GithubDailyStatsJpaMapper {

    public GithubDailyStatsJpaEntity toJpaEntity(GithubDailyStats stat, Long id) {
        return new GithubDailyStatsJpaEntity(
                id,
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
}
