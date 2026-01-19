package com.process.clash.adapter.persistence.github;

import com.process.clash.domain.github.entity.GithubDailyStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({GithubDailyStatsJpaAdapter.class, GithubDailyStatsJpaMapper.class})
class GithubDailyStatsJpaAdapterTest {

    @Autowired
    private GithubDailyStatsJpaAdapter adapter;

    @Autowired
    private GithubDailyStatsJpaRepository repository;

    @Test
    void upsertOverwritesExistingRow() {
        GithubDailyStats first = new GithubDailyStats(
                1L,
                LocalDate.of(2026, 1, 19),
                1,
                2,
                3,
                4,
                10L,
                5L,
                Instant.parse("2026-01-19T00:00:00Z")
        );

        GithubDailyStats updated = new GithubDailyStats(
                1L,
                LocalDate.of(2026, 1, 19),
                9,
                8,
                7,
                6,
                100L,
                50L,
                Instant.parse("2026-01-19T01:00:00Z")
        );

        adapter.upsertAll(List.of(first));
        adapter.upsertAll(List.of(updated));

        List<GithubDailyStatsJpaEntity> rows = repository.findByUserIdAndStudyDateIn(
                1L,
                List.of(LocalDate.of(2026, 1, 19))
        );

        assertThat(rows).hasSize(1);
        assertThat(rows.get(0).getCommitCount()).isEqualTo(9);
        assertThat(rows.get(0).getAdditions()).isEqualTo(100L);
    }
}
