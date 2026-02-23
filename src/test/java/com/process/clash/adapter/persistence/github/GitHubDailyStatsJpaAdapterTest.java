package com.process.clash.adapter.persistence.github;

import com.process.clash.domain.github.entity.GitHubDailyStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({GitHubDailyStatsJpaAdapter.class, GitHubDailyStatsJpaMapper.class})
class GitHubDailyStatsJpaAdapterTest {

    @Autowired
    private GitHubDailyStatsJpaAdapter adapter;

    @Autowired
    private GitHubDailyStatsJpaRepository repository;

    @Test
    void upsertOverwritesExistingRow() {
        GitHubDailyStats first = new GitHubDailyStats(
                1L,
                LocalDate.of(2026, 1, 19),
                1,
                2,
                3,
                4,
                10L,
                5L,
                "repo-a",
                "repo-pr-a",
                Instant.parse("2026-01-19T00:30:00Z"),
                Instant.parse("2026-01-19T09:30:00Z"),
                1,
                2,
                3,
                Instant.parse("2026-01-19T00:00:00Z")
        );

        GitHubDailyStats updated = new GitHubDailyStats(
                1L,
                LocalDate.of(2026, 1, 19),
                9,
                8,
                7,
                6,
                100L,
                50L,
                "repo-z",
                "repo-pr-z",
                Instant.parse("2026-01-19T01:30:00Z"),
                Instant.parse("2026-01-19T11:30:00Z"),
                4,
                5,
                6,
                Instant.parse("2026-01-19T01:00:00Z")
        );

        adapter.upsertAll(List.of(first));
        adapter.upsertAll(List.of(updated));

        List<GitHubDailyStatsJpaEntity> rows = repository.findByUserIdAndStudyDateIn(
                1L,
                List.of(LocalDate.of(2026, 1, 19))
        );

        assertThat(rows).hasSize(1);
        assertThat(rows.get(0).getCommitCount()).isEqualTo(9);
        assertThat(rows.get(0).getAdditions()).isEqualTo(100L);
        assertThat(rows.get(0).getTopCommitRepo()).isEqualTo("repo-z");
        assertThat(rows.get(0).getTopPrRepo()).isEqualTo("repo-pr-z");
        assertThat(rows.get(0).getPrMergedCount()).isEqualTo(4);
        assertThat(rows.get(0).getPrOpenCount()).isEqualTo(5);
        assertThat(rows.get(0).getPrClosedCount()).isEqualTo(6);
    }
}
