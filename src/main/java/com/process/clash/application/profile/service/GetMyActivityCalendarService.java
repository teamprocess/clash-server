package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetMyActivityCalendarData;
import com.process.clash.application.profile.port.in.GetMyActivityCalendarUsecase;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyActivityCalendarService implements GetMyActivityCalendarUsecase {

    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;

    @Override
    public GetMyActivityCalendarData.Result execute(GetMyActivityCalendarData.Command command) {
        YearMonth month = command.month();
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.plusMonths(1).atDay(1);
        int limit = month.lengthOfMonth();

        List<Object[]> rows = userStudyTimeRepositoryPort.findDailyDataByUserIds(
                List.of(command.actor().id()),
                startDate,
                endDate,
                PageRequest.of(0, limit)
        );

        List<GetMyActivityCalendarData.CalendarDay> calendar = rows.stream()
                .map(row -> {
                    LocalDate date = ((Date) row[1]).toLocalDate();
                    int studyTime = ((Number) row[2]).intValue();
                    return GetMyActivityCalendarData.CalendarDay.of(date.toString(), studyTime);
                })
                .toList();

        return GetMyActivityCalendarData.Result.from(calendar);
    }
}
