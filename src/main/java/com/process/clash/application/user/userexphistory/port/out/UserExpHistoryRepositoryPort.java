package com.process.clash.application.user.userexphistory.port.out;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserExpHistoryRepositoryPort {
    UserExpHistory save(UserExpHistory userExpHistory);
    List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    double findAverageExpByUserIdAndCategoryAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    Map<Long, Double> findAverageExpForBattles(Long userId, List<Battle> battles);
    List<Streak> findStreakByUserId(Long userId, LocalDate standard);
    List<Variation> findVariationByUserId(Long userId, LocalDate standard);
}
