package com.process.clash.adapter.persistence.roadmap.missionhistory;

import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaEntity;
import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.domain.roadmap.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMissionHistoryJpaMapper {

    public UserMissionHistoryJpaEntity toJpaEntity(UserMissionHistory history) {
        return null; // TODO: impl userjparepository
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
