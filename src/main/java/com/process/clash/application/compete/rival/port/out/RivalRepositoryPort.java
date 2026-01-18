package com.process.clash.application.compete.rival.port.out;

import com.process.clash.domain.rival.entity.Rival;

import java.util.List;

public interface RivalRepositoryPort {

    Rival save(Rival rival);
    void saveAll(List<Rival> rivals);
    List<Rival> findAllByMyId(Long myId);
    int countAllByMyId(Long myId);
    int countAllByOpponentId(Long opponentId);
}
