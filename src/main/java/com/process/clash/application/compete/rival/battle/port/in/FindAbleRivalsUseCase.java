package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.FindAbleRivalsData;

public interface FindAbleRivalsUseCase {

    FindAbleRivalsData.Result execute(FindAbleRivalsData.Command command);
}
