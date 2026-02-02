package com.process.clash.application.github.service;

import com.process.clash.application.github.service.GithubReviewAggregator.ReviewContribution;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GithubReviewAggregatorTest {

    @Test
    void countsDistinctPrIdsByStudyDate() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        StudyDateCalculator calculator = new StudyDateCalculator(kst, 6);
        GithubReviewAggregator aggregator = new GithubReviewAggregator();

        Instant pr1Morning = ZonedDateTime.of(LocalDate.of(2026, 1, 19), LocalTime.of(7, 0), kst).toInstant();
        Instant pr1Later = ZonedDateTime.of(LocalDate.of(2026, 1, 19), LocalTime.of(8, 0), kst).toInstant();
        Instant pr2Early = ZonedDateTime.of(LocalDate.of(2026, 1, 19), LocalTime.of(5, 30), kst).toInstant();
        Instant pr2Morning = ZonedDateTime.of(LocalDate.of(2026, 1, 19), LocalTime.of(7, 30), kst).toInstant();

        List<ReviewContribution> contributions = List.of(
                new ReviewContribution(pr1Morning, "PR1"),
                new ReviewContribution(pr1Later, "PR1"),
                new ReviewContribution(pr2Early, "PR2"),
                new ReviewContribution(pr2Morning, "PR2")
        );

        Map<LocalDate, Integer> result = aggregator.countDistinctReviewedPrsByStudyDate(contributions, calculator);

        assertThat(result.get(LocalDate.of(2026, 1, 18))).isEqualTo(1);
        assertThat(result.get(LocalDate.of(2026, 1, 19))).isEqualTo(2);
    }
}
