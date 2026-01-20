package com.process.clash.application.compete.rival.port.out;

import com.process.clash.application.compete.rival.data.RivalInfoForGraph;
import com.process.clash.domain.rival.entity.Rival;

import java.util.List;
import java.util.Map;

public interface RivalRepositoryPort {

    Rival save(Rival rival);
    void saveAll(List<Rival> rivals);
    List<Rival> findAllByMyId(Long myId);
    int countAllByMyId(Long myId);
    List<Map<String, Object>> countAllByOpponentIdsGrouped(List<Long> opponentIds);
    List<RivalInfoForGraph> findRivalInfoForGraphByMyId(Long myId);
}
