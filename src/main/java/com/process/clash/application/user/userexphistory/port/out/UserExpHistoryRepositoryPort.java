package com.process.clash.application.user.userexphistory.port.out;

import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserExpHistoryRepositoryPort {
    UserExpHistory save(UserExpHistory userExpHistory);
    List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate);
    List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate);
    List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate);
    double findAverageExpByUserIdAndCategoryAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    Map<Long, Double> findAverageExpByUserIdAndBattles(Long userId, List<Battle> battles);
}
