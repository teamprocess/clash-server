package com.process.clash.adapter.persistence.roadmap.missionhistory;

import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserMissionHistoryPersistenceAdapter implements UserMissionHistoryRepositoryPort {

    private final UserMissionHistoryJpaRepository userMissionHistoryJpaRepository;
    private final UserMissionHistoryJpaMapper userMissionHistoryJpaMapper;

    @Override
    public void save(UserMissionHistory history) {
        userMissionHistoryJpaRepository.save(userMissionHistoryJpaMapper.toJpaEntity(history));
    }

    @Override
    public Optional<UserMissionHistory> findById(Long id) {
        return userMissionHistoryJpaRepository.findById(id).map(userMissionHistoryJpaMapper::toDomain);
    }

    @Override
    public List<UserMissionHistory> findAllByUserId(Long userId) {
        return userMissionHistoryJpaRepository.findAllByUserId(userId).stream().map(userMissionHistoryJpaMapper::toDomain).toList();
    }
}
