package com.process.clash.application.rival.port.out;

import com.process.clash.domain.rival.entity.Rival;

public interface RivalRepositoryPort {

    Rival save(Rival rival);
    int countAllByMy_Id(Long myId);
    int countAllByOpponent_Id(Long opponentId);
}
