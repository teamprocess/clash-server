package com.process.clash.application.compete.rival.rival.port.out;

import com.process.clash.domain.rival.rival.entity.Rival;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RivalRepositoryPort {

    Rival save(Rival rival);
    void saveAll(List<Rival> rivals);
    List<Rival> findAllByUserId(Long myId);
    int countAllByUserId(Long myId);
    List<Map<String, Object>> countAllByOpponentIdsGrouped(List<Long> opponentIds);
    List<Long> findOpponentIdByUserId(Long myId);
    Long findOpponentIdByIdAndUserId(Long id, Long userId);
    Optional<Rival> findById(Long id);
}
