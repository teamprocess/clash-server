package com.process.clash.application.compete.rival.port.out;

import com.process.clash.domain.rival.entity.Rival;

import java.util.List;

public interface RivalRepositoryPort {

    Rival save(Rival rival);
    void saveAll(List<Rival> rivals);
    List<Rival> findAllByMy_Id(Long myId);
    int countAllByMy_Id(Long myId);
    int countAllByOpponent_Id(Long opponentId);
}
