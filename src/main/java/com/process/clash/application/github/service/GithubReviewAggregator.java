package com.process.clash.application.github.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GithubReviewAggregator {

    public Map<LocalDate, Integer> countDistinctReviewedPrsByStudyDate(
            List<ReviewContribution> contributions,
            StudyDateCalculator calculator
    ) {
        Map<LocalDate, Set<String>> prIdsByDate = new HashMap<>();
        // 같은 날짜에 같은 PR을 여러 번 리뷰한 경우 중복 제거
        for (ReviewContribution contribution : contributions) {
            LocalDate studyDate = calculator.toStudyDate(contribution.occurredAt());
            prIdsByDate
                    .computeIfAbsent(studyDate, key -> new HashSet<>())
                    .add(contribution.pullRequestId());
        }

        Map<LocalDate, Integer> result = new HashMap<>();
        // 날짜별로 고유 PR 개수 집계
        for (Map.Entry<LocalDate, Set<String>> entry : prIdsByDate.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }

    public record ReviewContribution(Instant occurredAt, String pullRequestId) {
    }
}
