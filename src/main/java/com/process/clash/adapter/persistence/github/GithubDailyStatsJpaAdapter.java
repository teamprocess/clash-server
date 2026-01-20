package com.process.clash.adapter.persistence.github;

import com.process.clash.application.github.port.out.GithubDailyStatsStorePort;
import com.process.clash.domain.github.entity.GithubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GithubDailyStatsJpaAdapter implements GithubDailyStatsStorePort {

    private final GithubDailyStatsJpaRepository repository;
    private final GithubDailyStatsJpaMapper mapper;

    @Override
    @Transactional
    public void upsertAll(List<GithubDailyStats> stats) {
        if (stats == null || stats.isEmpty()) {
            return;
        }

        Map<Long, List<GithubDailyStats>> statsByUser = stats.stream()
                .collect(Collectors.groupingBy(GithubDailyStats::userId));

        List<GithubDailyStatsJpaEntity> entities = new ArrayList<>(stats.size());
        for (Map.Entry<Long, List<GithubDailyStats>> entry : statsByUser.entrySet()) {
            Long userId = entry.getKey();
            List<GithubDailyStats> userStats = entry.getValue();
            List<LocalDate> dates = userStats.stream()
                    .map(GithubDailyStats::studyDate)
                    .distinct()
                    .toList();

            Map<LocalDate, Long> existingIds = new HashMap<>();
            for (GithubDailyStatsJpaEntity entity : repository.findByUserIdAndStudyDateIn(userId, dates)) {
                existingIds.put(entity.getStudyDate(), entity.getUserId());
            }

            for (GithubDailyStats stat : userStats) {
                Long existingId = existingIds.get(stat.studyDate());
                entities.add(mapper.toJpaEntity(stat));
            }
        }

        repository.saveAll(entities);
    }
}
