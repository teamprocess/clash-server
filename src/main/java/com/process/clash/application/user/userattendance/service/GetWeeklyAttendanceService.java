package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userattendance.data.GetWeeklyAttendanceData;
import com.process.clash.application.user.userattendance.port.in.GetWeeklyAttendanceUseCase;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWeeklyAttendanceService implements GetWeeklyAttendanceUseCase {

    private final UserAttendanceRepositoryPort userAttendanceRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetWeeklyAttendanceData.Result execute(GetWeeklyAttendanceData.Command command) {
        LocalDate today = UserAttendance.currentAttendanceDate();

        // 이번 주 일요일(주 시작) ~ 토요일(주 끝)
        // DayOfWeek: MONDAY=1 ... SUNDAY=7, 7 % 7 = 0
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() % 7);
        LocalDate weekEnd = weekStart.plusDays(6);

        Long userId = command.actor().id();

        List<UserAttendance> records = userAttendanceRepositoryPort
                .findByUserIdBetweenDates(userId, weekStart, weekEnd);

        Map<LocalDate, Boolean> attendanceByDate = records.stream()
                .collect(Collectors.toMap(UserAttendance::attendanceDate, UserAttendance::isAttended));

        List<GetWeeklyAttendanceData.AttendanceDay> days = Stream.iterate(weekStart, d -> d.plusDays(1))
                .limit(7)
                .map(date -> new GetWeeklyAttendanceData.AttendanceDay(
                        date,
                        date.getDayOfWeek(),
                        attendanceByDate.getOrDefault(date, false)
                ))
                .toList();

        int weekNumber = calculateWeekNumber(userId, weekStart);

        int currentStreak = userRepositoryPort.findById(userId)
                .orElseThrow(UserNotFoundException::new)
                .currentAttendanceStreak();

        return new GetWeeklyAttendanceData.Result(weekNumber, weekStart, weekEnd, days, currentStreak);
    }

    private int calculateWeekNumber(Long userId, LocalDate currentWeekStart) {
        LocalDate firstRecordDate = userAttendanceRepositoryPort
                .findEarliestAttendanceDateByUserId(userId)
                .orElse(currentWeekStart);

        // 첫 레코드가 속한 주의 일요일
        LocalDate firstWeekStart = firstRecordDate.minusDays(firstRecordDate.getDayOfWeek().getValue() % 7);

        return (int) ChronoUnit.WEEKS.between(firstWeekStart, currentWeekStart) + 1;
    }
}