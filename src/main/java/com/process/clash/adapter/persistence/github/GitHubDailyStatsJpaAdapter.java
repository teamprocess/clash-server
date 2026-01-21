package com.process.clash.adapter.persistence.github;

import com.process.clash.application.github.port.out.GithubDailyStatsStorePort;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GitHubDailyStatsJpaAdapter implements GithubDailyStatsStorePort {

    private final GitHubDailyStatsJpaRepository repository;
    private final GitHubDailyStatsJpaMapper mapper;

    @Override
    @Transactional
    public void upsertAll(List<GitHubDailyStats> stats) {
        if (stats == null || stats.isEmpty()) {
            return;
        }

        Map<Long, List<GitHubDailyStats>> statsByUser = stats.stream()
                .collect(Collectors.groupingBy(GitHubDailyStats::userId));

        List<GitHubDailyStatsJpaEntity> entities = new ArrayList<>(stats.size());

        for (Map.Entry<Long, List<GitHubDailyStats>> entry : statsByUser.entrySet()) {
            Long userId = entry.getKey();
            List<GitHubDailyStats> userStats = entry.getValue();

            List<LocalDate> dates = userStats.stream()
                    .map(GitHubDailyStats::studyDate)
                    .distinct()
                    .toList();

            // 기존 엔티티를 날짜별로 맵핑
            Map<LocalDate, GitHubDailyStatsJpaEntity> existingEntitiesByDate = repository
                    .findByUserIdAndStudyDateIn(userId, dates)
                    .stream()
                    .collect(Collectors.toMap(
                            GitHubDailyStatsJpaEntity::getStudyDate,
                            entity -> entity
                    ));

            // upsert 처리
            for (GitHubDailyStats stat : userStats) {
                GitHubDailyStatsJpaEntity entity = existingEntitiesByDate.get(stat.studyDate());

                if (entity != null) {
                    mapper.updateEntity(entity, stat);
                } else {
                    entity = mapper.toJpaEntity(stat);
                }

                entities.add(entity);
            }
        }

        repository.saveAll(entities);
    }
}
