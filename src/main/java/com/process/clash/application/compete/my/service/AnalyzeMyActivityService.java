package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;
import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.compete.my.port.in.AnalyzeMyActivityUseCase;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyzeMyActivityService implements AnalyzeMyActivityUseCase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;

    @Override
    public AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command) {

        LocalDate standard = LocalDate.now().minusYears(1);

        Map<List<Streak>, List<Variation>> result = switch (command.category()) {
            case GITHUB -> gitHub(command.actor().id(), standard);
            case EXP -> exp(command.actor().id(), standard);
            case SOLVED_AC -> new HashMap<>(); //TODO: 나중에 구현
            case ACTIVE_TIME -> studyTime(command.actor().id(), standard);
        };

        return AnalyzeMyActivityData.Result.from(command.category(), result);
    }

    private Map<List<Streak>, List<Variation>> gitHub(Long userId, LocalDate standard) {

        List<Streak> streaks = gitHubDailyStatsQueryPort.findStreakByUserId(userId, standard);
        List<Variation> variations = gitHubDailyStatsQueryPort.findVaricationByUserId(userId, standard);

        return Map.of(streaks, variations);
    }

    private Map<List<Streak>, List<Variation>> exp(Long userId, LocalDate standard) {

        List<Streak> streaks = userExpHistoryRepositoryPort.findStreakByUserId(userId, standard);
        List<Variation> variations = userExpHistoryRepositoryPort.findVariationByUserId(userId, standard);

        return Map.of(streaks, variations);
    }

    private Map<List<Streak>, List<Variation>> studyTime(Long userId, LocalDate standard) {

        LocalDate now = LocalDate.now();

        List<Streak> streaks = userStudyTimeRepositoryPort.findStreakByUserId(userId, standard);

        LocalDateTime startOfDay = now.atTime(6, 0, 0);
        LocalDateTime endOfDay = now.plusDays(1).atTime(6, 0, 0);

        Long todayActiveTime = studySessionRepositoryPort.getTotalStudyTimeInSeconds(
                userId,
                startOfDay,
                endOfDay
        );

        int activeTimeValue = (todayActiveTime != null) ? todayActiveTime.intValue() : 0;
        Streak todayStreak = new Streak(now, activeTimeValue);
        streaks.add(todayStreak);

        List<Variation> variations = userStudyTimeRepositoryPort.findVariationByUserId(userId, standard);

        int currentMonth = now.getMonthValue();
        int daysInMonth = now.lengthOfMonth(); // 해당 월의 실제 일수 (28, 29, 30, 31)

        boolean monthExists = false;
        List<Variation> updatedVariations = new ArrayList<>();

        // 최대 12개 데이터여서 반복문 돌렸습니다.
        for (Variation v : variations) {
            if (v.month().equals(currentMonth)) {
                // 기존 평균에 오늘 데이터 추가해서 재계산
                double newAvg = (v.avgVariationPerMonth() * (daysInMonth - 1) + activeTimeValue) / daysInMonth;
                updatedVariations.add(new Variation(currentMonth, newAvg));
                monthExists = true;
            } else {
                updatedVariations.add(v);
            }
        }

        if (!monthExists) {
            updatedVariations.add(new Variation(currentMonth, (double) activeTimeValue / daysInMonth));
        }

        return Map.of(streaks, updatedVariations);
    }
}
