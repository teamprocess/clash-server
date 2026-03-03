package com.process.clash.application.user.exp.service;

import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.github.service.StudyDateCalculator;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubExpGrantService {

    private static final int MAX_COMMITS = 50;
    private static final int MAX_PRS = 5;
    private static final int MAX_REVIEWS = 5;
    private static final int MAX_ISSUES = 5;
    private static final int EXP_PER_COMMIT = 50;
    private static final int EXP_PER_PR = 100;
    private static final int EXP_PER_REVIEW = 100;
    private static final int EXP_PER_ISSUE = 10;

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final StudyDateCalculator studyDateCalculator;

    @Transactional
    public void grantForToday() {
        LocalDate studyDate = studyDateCalculator.toStudyDate(Instant.now());
        List<GitHubDailyStats> statsList = gitHubDailyStatsQueryPort.findAllByStudyDate(studyDate);

        if (statsList.isEmpty()) {
            log.info("GitHub EXP 지급 대상 없음. studyDate={}", studyDate);
            return;
        }

        log.info("GitHub EXP 지급 시작. studyDate={}, users={}", studyDate, statsList.size());

        for (GitHubDailyStats stats : statsList) {
            try {
                grantForUser(stats, studyDate);
            } catch (Exception e) {
                log.error("GitHub EXP 지급 실패. userId={}, studyDate={}", stats.userId(), studyDate, e);
            }
        }
    }

    private void grantForUser(GitHubDailyStats stats, LocalDate studyDate) {
        Optional<User> userOpt = userRepositoryPort.findByIdForUpdate(stats.userId());
        if (userOpt.isEmpty()) {
            return;
        }
        User user = userOpt.get();

        int newEarnExp = calculateExp(stats);

        Optional<UserExpHistory> existing = userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(
                stats.userId(), studyDate, ExpActingCategory.GITHUB
        );

        int delta;
        if (existing.isPresent()) {
            delta = newEarnExp - existing.get().earnExp();
            if (delta == 0) {
                return;
            }
            userExpHistoryRepositoryPort.save(new UserExpHistory(
                    existing.get().id(),
                    existing.get().createdAt(),
                    studyDate,
                    newEarnExp,
                    ExpActingCategory.GITHUB,
                    stats.userId()
            ));
        } else {
            if (newEarnExp == 0) {
                return;
            }
            delta = newEarnExp;
            userExpHistoryRepositoryPort.save(new UserExpHistory(
                    null,
                    Instant.now(),
                    studyDate,
                    newEarnExp,
                    ExpActingCategory.GITHUB,
                    stats.userId()
            ));
        }

        userRepositoryPort.save(user.addExp(delta));
        log.debug("GitHub EXP 지급 완료. userId={}, delta={}, newEarnExp={}", stats.userId(), delta, newEarnExp);
    }

    private int calculateExp(GitHubDailyStats stats) {
        return Math.min(stats.commitCount(), MAX_COMMITS) * EXP_PER_COMMIT
                + Math.min(stats.prCount(), MAX_PRS) * EXP_PER_PR
                + Math.min(stats.reviewedPrCount(), MAX_REVIEWS) * EXP_PER_REVIEW
                + Math.min(stats.issueCount(), MAX_ISSUES) * EXP_PER_ISSUE;
    }
}
