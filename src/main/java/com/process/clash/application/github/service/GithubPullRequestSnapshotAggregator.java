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
            Map<LocalDate, Integer> createdCounts
    ) {
        Map<LocalDate, Integer> result = new HashMap<>();

        for (LocalDate studyDate : orderedStudyDates) {
            result.put(studyDate, createdCounts.getOrDefault(studyDate, 0));
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
}
