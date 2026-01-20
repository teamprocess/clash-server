package com.process.clash.application.user.userexphistory.port.out;

import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;

import java.time.LocalDate;
import java.util.List;

public interface UserExpHistoryRepositoryPort {
    UserExpHistory save(UserExpHistory userExpHistory);
    List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate);
    List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate);
    List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate);
}
