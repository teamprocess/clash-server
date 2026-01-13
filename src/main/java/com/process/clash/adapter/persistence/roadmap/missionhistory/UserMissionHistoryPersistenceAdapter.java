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
    private final com.process.clash.adapter.persistence.user.UserJpaRepository userJpaRepository;
    private final com.process.clash.adapter.persistence.roadmap.mission.MissionJpaRepository missionJpaRepository;

    @Override
    public void save(UserMissionHistory history) {
        com.process.clash.adapter.persistence.user.UserJpaEntity userEntity = userJpaRepository.getReferenceById(history.getUserId());
        com.process.clash.adapter.persistence.roadmap.mission.MissionJpaEntity missionEntity = missionJpaRepository.getReferenceById(history.getMissionId());
        userMissionHistoryJpaRepository.save(userMissionHistoryJpaMapper.toJpaEntity(history, userEntity, missionEntity));
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
