package com.process.clash.application.compete.rival.rival.port.out;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import com.process.clash.domain.rival.rival.entity.Rival;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface RivalRepositoryPort {

    void saveAndFlush(Rival rival);
    Rival save(Rival rival);
    void saveAll(List<Rival> rivals);
    List<Rival> findAllByUserId(Long myId);
    int countAllByUserId(Long myId);
    List<Map<String, Object>> countAllByOpponentIdsGrouped(List<Long> opponentIds);
    List<Long> findOpponentIdByUserId(Long myId);
    Long findOpponentIdByIdAndUserId(Long id, Long userId);
    Optional<Rival> findById(Long id);
    List<AbleRivalInfoForBattle> findAbleToBattleRivals(Long userId);
    List<Rival> findByIdIn(Set<Long> ids);
}
