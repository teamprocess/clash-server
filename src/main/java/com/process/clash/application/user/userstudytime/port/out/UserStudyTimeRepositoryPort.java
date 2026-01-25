package com.process.clash.application.user.userstudytime.port.out;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserStudyTimeRepositoryPort {

    UserStudyTime save(UserStudyTime userStudyTime);
    Optional<UserStudyTime> findByUserIdAndDate(Long userId, LocalDate date);

    List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);

    double findAverageStudyTimeByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    List<Streak> findStreakByUserId(Long userId, LocalDate startDate, LocalDate endDate);
    List<Variation> findVariationByUserId(Long userId, LocalDate startDate, LocalDate endDate);
}
