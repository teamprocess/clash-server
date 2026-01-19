package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;
import com.process.clash.application.compete.my.port.in.GetCompareWithYesterdayUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetCompareWithYesterdayService implements GetCompareWithYesterdayUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;

    @Override
    public GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command) {

        LocalDate today = LocalDate.now();

        LocalDate yesterday = today.minusDays(1);

        UserStudyTime yesterdayUserStudyTime =
                userStudyTimeRepositoryPort.findByUserIdAndDate(command.actor().id(), yesterday)
                        .orElseThrow();

        LocalDateTime startOfDay = today.atTime(6, 0, 0);
        LocalDateTime endOfDay = today.plusDays(1).atTime(6, 0, 0);

        Long todayActiveTime = studySessionRepositoryPort.getTotalStudyTimeInSeconds(
                command.actor().id(),
                startOfDay,
                endOfDay
        );

        return null;
    }
}
