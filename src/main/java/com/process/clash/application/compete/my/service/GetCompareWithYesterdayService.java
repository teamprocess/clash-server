package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;
import com.process.clash.application.compete.my.port.in.GetCompareWithYesterdayUseCase;
import com.process.clash.application.githubinfo.exception.exception.notfound.GitHubInfoNotFoundException;
import com.process.clash.application.githubinfo.port.out.GitHubInfoRepositoryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userstudytime.exception.exception.notfound.UserStudyTimeNotFoundException;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetCompareWithYesterdayService implements GetCompareWithYesterdayUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final GitHubInfoRepositoryPort gitHubInfoRepositoryPort;

    @Override
    public GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command) {

        LocalDate today = LocalDate.now();

        LocalDate yesterday = today.minusDays(1);

        Long yesterdayActiveTime =
                userStudyTimeRepositoryPort.findByUserIdAndDate(command.actor().id(), yesterday)
                        .orElseThrow(UserStudyTimeNotFoundException::new)
                        .totalStudyTimeSeconds();

        LocalDateTime startOfDay = today.atTime(6, 0, 0);
        LocalDateTime endOfDay = today.plusDays(1).atTime(6, 0, 0);

        Long todayActiveTime = studySessionRepositoryPort.getTotalStudyTimeInSeconds(
                command.actor().id(),
                startOfDay,
                endOfDay
        );

        Integer yesterdayContributions =
                gitHubInfoRepositoryPort.findByUserIdAndDate(command.actor().id(), yesterday)
                        .orElseThrow(GitHubInfoNotFoundException::new)
                        .contributionCount();

        Integer todayContributions =
                gitHubInfoRepositoryPort.findByUserIdAndDate(command.actor().id(), today)
                        .orElseThrow(GitHubInfoNotFoundException::new)
                        .contributionCount();

        return null;
    }
}
