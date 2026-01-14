package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.entity.UserMissionHistory;

import java.util.List;
import java.util.Optional;

public interface UserMissionHistoryRepositoryPort {
    void save(UserMissionHistory history);
    Optional<UserMissionHistory> findById(Long id);
    List<UserMissionHistory> findAllByUserId(Long userId);
}
