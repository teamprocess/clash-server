package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindAbleRivalsData;
import com.process.clash.application.compete.rival.battle.port.in.FindAbleRivalsUseCase;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAbleRivalsService implements FindAbleRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    public FindAbleRivalsData.Result execute(FindAbleRivalsData.Command command) {

        List<AbleRivalInfoForBattle> rivals = rivalRepositoryPort.findAbleToBattleRivals(command.actor().id());

        if (rivals.isEmpty()) {
            throw new RivalNotFoundException();
        }

        return FindAbleRivalsData.Result.from(rivals);
    }
}
