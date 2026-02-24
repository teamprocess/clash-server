package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;
import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.compete.my.port.in.AnalyzeMyActivityUseCase;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzeMyActivityService implements AnalyzeMyActivityUseCase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final RecordSessionRepositoryPort recordSessionRepositoryPort;

    private record ActivityData(List<Streak> streaks, List<Variation> variations) {}

    @Override
    public AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command) {

        LocalDate endDate = LocalDate.now().plusDays(1); // 오늘 포함
        LocalDate startDate = endDate.minusMonths(12);   // 정확히 12개월 기간

        ActivityData data = switch (command.category()) {
            case GITHUB -> gitHub(command.actor().id(), startDate, endDate);
            case EXP -> exp(command.actor().id(), startDate, endDate);
            case SOLVED_AC -> new ActivityData(List.of(), List.of()); //TODO: 나중에 구현
            case ACTIVE_TIME -> studyTime(command.actor().id(), startDate, endDate);
        };

        return AnalyzeMyActivityData.Result.of(command.category(), data.streaks(), data.variations());
    }

    private ActivityData gitHub(Long userId, LocalDate startDate, LocalDate endDate) {

        List<Streak> streaks = computeColorRatios(gitHubDailyStatsQueryPort.findStreakByUserId(userId, startDate, endDate));
        List<Variation> variations = gitHubDailyStatsQueryPort.findVaricationByUserId(userId, startDate, endDate);

        return new ActivityData(streaks, variations);
    }

    private ActivityData exp(Long userId, LocalDate startDate, LocalDate endDate) {

        List<Streak> streaks = computeColorRatios(userExpHistoryRepositoryPort.findStreakByUserId(userId, startDate, endDate));
        List<Variation> variations = userExpHistoryRepositoryPort.findVariationByUserId(userId, startDate, endDate);

        return new ActivityData(streaks, variations);
    }

    private ActivityData studyTime(Long userId, LocalDate startDate, LocalDate endDate) {

        LocalDate now = LocalDate.now();

        List<Streak> streaks = new ArrayList<>(userStudyTimeRepositoryPort.findStreakByUserId(userId, startDate, endDate));

        LocalDateTime startOfDay = now.atTime(6, 0, 0);
        LocalDateTime endOfDay = now.plusDays(1).atTime(6, 0, 0);

        Long todayActiveTime = recordSessionRepositoryPort.getTotalStudyTimeInSeconds(
                userId,
                startOfDay,
                endOfDay
        );

        int activeTimeValue = (todayActiveTime != null) ? todayActiveTime.intValue() : 0;
        Streak todayStreak = new Streak(now, activeTimeValue);
        streaks.add(todayStreak);
        List<Streak> enrichedStreaks = computeColorRatios(streaks);

        List<Variation> variations = userStudyTimeRepositoryPort.findVariationByUserId(userId, startDate, endDate);

        int currentMonth = now.getMonthValue();

        boolean monthExists = false;
        List<Variation> updatedVariations = new ArrayList<>();

        // 최대 12개 데이터여서 반복문 돌렸습니다.
        for (Variation v : variations) {
            if (v.month().equals(currentMonth)) {
                // 기존 총합에 오늘 데이터 추가
                double newTotal = v.totalVariationPerMonth() + activeTimeValue;
                updatedVariations.add(new Variation(currentMonth, newTotal));
                monthExists = true;
            } else {
                updatedVariations.add(v);
            }
        }

        if (!monthExists) {
            updatedVariations.add(new Variation(currentMonth, (double) activeTimeValue));
        }

        return new ActivityData(enrichedStreaks, updatedVariations);
    }

    private List<Streak> computeColorRatios(List<Streak> streaks) {

        if (streaks.isEmpty()) return streaks;

        int max = streaks.stream()
                .map(Streak::detailedInfo)
                .filter(v -> v != null)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        if (max == 0) {
            return streaks.stream().map(s -> new Streak(s.date(), s.detailedInfo(), 0)).toList();
        }

        return streaks.stream()
                .map(s -> {
                    int value = s.detailedInfo() != null ? s.detailedInfo() : 0;
                    int ratio = (int) Math.round((double) value / max * 100);
                    return new Streak(s.date(), s.detailedInfo(), ratio);
                })
                .toList();
    }
}
