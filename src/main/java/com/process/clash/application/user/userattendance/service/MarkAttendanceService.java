package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userattendance.data.MarkAttendanceData;
import com.process.clash.application.user.userattendance.exception.exception.conflict.AlreadyAttendedException;
import com.process.clash.application.user.userattendance.port.in.MarkAttendanceUseCase;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MarkAttendanceService implements MarkAttendanceUseCase {

    private static final int DAILY_COOKIE_REWARD = 300;
    private static final int WEEKLY_COOKIE_REWARD = 1000;
    private static final int DAYS_IN_WEEK = 7;

    private final UserAttendanceRepositoryPort userAttendanceRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    @Override
    public MarkAttendanceData.Result execute(MarkAttendanceData.Command command) {
        Long userId = command.actor().id();
        LocalDate attendanceDate = UserAttendance.currentAttendanceDate();

        UserAttendance attendance = userAttendanceRepositoryPort
                .findByUserIdAndAttendanceDate(userId, attendanceDate)
                .orElseGet(() -> userAttendanceRepositoryPort.save(UserAttendance.create(userId, attendanceDate)));

        if (attendance.isAttended()) {
            throw new AlreadyAttendedException();
        }

        userAttendanceRepositoryPort.save(attendance.markAttend());

        User user = userRepositoryPort.findByIdForUpdate(userId)
                .orElseThrow(UserNotFoundException::new);

        boolean isFullWeek = isFullWeekCompleted(userId, attendanceDate);
        int earnedCookies = isFullWeek ? WEEKLY_COOKIE_REWARD : DAILY_COOKIE_REWARD;
        GoodsActingCategory category = isFullWeek
                ? GoodsActingCategory.ATTENDANCE_WEEKLY_REWARD
                : GoodsActingCategory.ATTENDANCE_DAILY_REWARD;

        User updated = user.incrementAttendanceStreak().addCookie(earnedCookies);
        userRepositoryPort.save(updated);
        userGoodsHistoryRepositoryPort.save(new UserGoodsHistory(null, null, category, earnedCookies, null, userId));

        return new MarkAttendanceData.Result(updated.currentAttendanceStreak(), earnedCookies);
    }

    private boolean isFullWeekCompleted(Long userId, LocalDate attendanceDate) {
        if (attendanceDate.getDayOfWeek() != DayOfWeek.SATURDAY) {
            return false;
        }
        // 토요일(getValue()=6): 6일 빼면 해당 주 일요일
        LocalDate weekSunday = attendanceDate.minusDays(6);
        long weeklyCount = userAttendanceRepositoryPort
                .countAttendedByUserIdBetween(userId, weekSunday, attendanceDate);
        return weeklyCount == DAYS_IN_WEEK;
    }
}