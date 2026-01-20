package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.CompareWithRivalsData;
import com.process.clash.application.compete.rival.data.RivalInfoForGraph;
import com.process.clash.application.compete.rival.port.in.CompareWithRivalsUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.github.port.out.GithubDailyStatsQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompareWithRivalsService implements CompareWithRivalsUseCase {

    private final GithubDailyStatsQueryPort githubDailyStatsQueryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final

    @Override
    public CompareWithRivalsData.Result execute(CompareWithRivalsData.Command command) {

        List<RivalInfoForGraph> rivalInfoForGraphs = rivalRepositoryPort.findRivalInfoForGraphByMyId(command.actor().id());



        return null;
    }
}
