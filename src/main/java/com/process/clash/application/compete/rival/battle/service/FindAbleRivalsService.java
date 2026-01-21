package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindAbleRivalsData;
import com.process.clash.application.compete.rival.battle.port.in.FindAbleRivalsUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAbleRivalsService implements FindAbleRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    public FindAbleRivalsData.Result execute(FindAbleRivalsData.Command command) {

        return FindAbleRivalsData.Result.from(rivalRepositoryPort.findAbleToBattleRivals(command.actor().id()));
    }
}
