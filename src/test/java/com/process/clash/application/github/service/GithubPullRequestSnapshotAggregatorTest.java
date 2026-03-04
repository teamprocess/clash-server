package com.process.clash.application.github.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GithubPullRequestSnapshotAggregatorTest {

    private final GithubPullRequestSnapshotAggregator aggregator = new GithubPullRequestSnapshotAggregator();

    @Test
    void calculateOpenCounts_returnsDailyCreatedCounts() {
        List<LocalDate> dates = List.of(
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 2),
                LocalDate.of(2026, 2, 3)
        );

        Map<LocalDate, Integer> created = Map.of(
                LocalDate.of(2026, 2, 1), 3,
                LocalDate.of(2026, 2, 2), 1,
                LocalDate.of(2026, 2, 3), 0
        );
        Map<LocalDate, Integer> result = aggregator.calculateOpenCounts(dates, created);

        assertThat(result).containsEntry(LocalDate.of(2026, 2, 1), 3);
        assertThat(result).containsEntry(LocalDate.of(2026, 2, 2), 1);
        assertThat(result).containsEntry(LocalDate.of(2026, 2, 3), 0);
    }

    @Test
    void calculateOpenCounts_returnsZeroWhenDateMissing() {
        List<LocalDate> dates = List.of(
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 2)
        );
        Map<LocalDate, Integer> created = Map.of(LocalDate.of(2026, 2, 1), 2);

        Map<LocalDate, Integer> result = aggregator.calculateOpenCounts(dates, created);

        assertThat(result).containsEntry(LocalDate.of(2026, 2, 1), 2);
        assertThat(result).containsEntry(LocalDate.of(2026, 2, 2), 0);
    }

    @Test
    void selectTopRepository_returnsLexicographicallySmallestWhenTied() {
        Map<String, Integer> counts = Map.of(
                "zeta-repo", 5,
                "alpha-repo", 5,
                "middle-repo", 4
        );

        String selected = aggregator.selectTopRepository(counts);

        assertThat(selected).isEqualTo("alpha-repo");
    }
}
