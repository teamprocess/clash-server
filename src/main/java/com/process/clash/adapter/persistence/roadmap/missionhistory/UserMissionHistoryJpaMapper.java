package com.process.clash.adapter.persistence.roadmap.missionhistory;

import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaRepository;
import com.process.clash.adapter.persistence.user.UserJpaRepository;
import com.process.clash.domain.roadmap.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMissionHistoryJpaMapper {

    private final UserJpaRepository userJpaRepository;
    private final MissionJpaRepository missionJpaRepository;

    public UserMissionHistoryJpaEntity toJpaEntity(UserMissionHistory history) {
        return new UserMissionHistoryJpaEntity(
                history.getId(),
                userJpaRepository.getReferenceById(history.getUserId()),
                missionJpaRepository.getReferenceById(history.getMissionId()),
                history.isCleared(),
                history.getScore(),
                history.getCurrentQuestionIndex()
        );
    }

    public UserMissionHistory toDomain(UserMissionHistoryJpaEntity entity) {
        return new UserMissionHistory(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getMission() != null ? entity.getMission().getId() : null,
                entity.isCleared(),
                entity.getScore(),
                entity.getCurrentQuestionIndex()
        );
    }
}
