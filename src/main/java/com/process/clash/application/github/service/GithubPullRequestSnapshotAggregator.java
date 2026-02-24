package com.process.clash.application.github.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GithubPullRequestSnapshotAggregator {

    public Map<LocalDate, Integer> calculateOpenCounts(
            List<LocalDate> orderedStudyDates,
            int baselineOpenCount,
            Map<LocalDate, Integer> createdCounts,
            Map<LocalDate, Integer> mergedCounts,
            Map<LocalDate, Integer> closedCounts
    ) {
        Map<LocalDate, Integer> result = new HashMap<>();
        long runningOpen = Math.max(0, baselineOpenCount);

        for (LocalDate studyDate : orderedStudyDates) {
            int created = createdCounts.getOrDefault(studyDate, 0);
            int merged = mergedCounts.getOrDefault(studyDate, 0);
            int closed = closedCounts.getOrDefault(studyDate, 0);

            runningOpen = Math.max(0, runningOpen + created - merged - closed);
            result.put(studyDate, clampToInt(runningOpen));
        }

        return result;
    }

    public String selectTopRepository(Map<String, Integer> repositoryCounts) {
        String selectedRepo = null;
        int maxCount = -1;

        for (Map.Entry<String, Integer> entry : repositoryCounts.entrySet()) {
            String repository = entry.getKey();
            int count = entry.getValue();

            if (count > maxCount) {
                maxCount = count;
                selectedRepo = repository;
                continue;
            }

            if (count == maxCount && selectedRepo != null && repository.compareTo(selectedRepo) < 0) {
                selectedRepo = repository;
            }
        }

        return selectedRepo;
    }

    private int clampToInt(long value) {
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) value;
    }
}
