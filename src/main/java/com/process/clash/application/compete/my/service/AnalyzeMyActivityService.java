package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;
import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.compete.my.port.in.AnalyzeMyActivityUseCase;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.common.enums.TargetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyzeMyActivityService implements AnalyzeMyActivityUseCase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;

    @Override
    public AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command) {

        LocalDate standard = LocalDate.now().minusYears(1);

        Map<List<Streak>, List<Variation>> result = switch (command.category()) {
            case GITHUB -> gitHub(command.actor().id(), standard);
            case EXP -> exp(command.actor().id(), standard);
            case SOLVED_AC -> null; //TODO: 나중에 구현
            case ACTIVE_TIME -> studyTime(command.actor().id(), standard);
        };

        return null;
    }

    private Map<List<Streak>, List<Variation>> gitHub(Long userId, LocalDate standard) {

        List<Streak> streaks = gitHubDailyStatsQueryPort.findStreakByUserId(userId, standard);
        List<Variation> variations = gitHubDailyStatsQueryPort.findVaricationByUserId(userId, standard);

        return null;
    }

    private Map<List<Streak>, List<Variation>> exp(Long userId, LocalDate standard) {

        return null;
    }

    private Map<List<Streak>, List<Variation>> studyTime(Long userId, LocalDate standard) {

        return null;
    }
}
