package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetMyCalendarData;
import com.process.clash.application.profile.port.in.GetMyCalendarUsecase;
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
public class GetMyCalendarService implements GetMyCalendarUsecase {

    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;

    @Override
    public GetMyCalendarData.Result execute(GetMyCalendarData.Command command) {
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

        List<GetMyCalendarData.CalendarDay> calendar = rows.stream()
                .map(row -> {
                    LocalDate date = ((Date) row[1]).toLocalDate();
                    int studyTime = ((Number) row[2]).intValue();
                    return GetMyCalendarData.CalendarDay.of(date.toString(), studyTime);
                })
                .toList();

        return GetMyCalendarData.Result.from(calendar);
    }
}
