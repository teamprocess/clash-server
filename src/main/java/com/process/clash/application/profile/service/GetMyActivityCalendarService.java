package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetMyActivityCalendarData;
import com.process.clash.application.profile.port.in.GetMyActivityCalendarUsecase;
import com.process.clash.application.user.userstudytime.data.UserStudyTimeDailyDto;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
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

        List<UserStudyTimeDailyDto> rows = userStudyTimeRepositoryPort.findDailyDataByUserId(
                command.actor().id(),
                startDate,
                endDate,
                PageRequest.of(0, limit)
        );

        List<GetMyActivityCalendarData.CalendarDay> calendar = rows.stream()
                .map(row -> GetMyActivityCalendarData.CalendarDay.of(
                        row.date().toString(),
                        (int) row.studyTimeSeconds()
                ))
                .toList();

        return GetMyActivityCalendarData.Result.from(calendar);
    }
}
