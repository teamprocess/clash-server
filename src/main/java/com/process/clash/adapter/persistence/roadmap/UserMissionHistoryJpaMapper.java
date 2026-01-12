package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.domain.roadmap.UserMissionHistory;
import org.springframework.stereotype.Component;

@Component
public class UserMissionHistoryJpaMapper {

    public UserMissionHistoryJpaEntity toJpaEntity(UserMissionHistory history) {
        UserMissionHistoryJpaEntity e = history.getId() != null ? UserMissionHistoryJpaEntity.ofId(history.getId()) : new UserMissionHistoryJpaEntity();
        if (history.getUserId() != null) {
            e.setUser(UserJpaEntity.ofId(history.getUserId()));
        }
        if (history.getMissionId() != null) {
            e.setMission(MissionJpaEntity.ofId(history.getMissionId()));
        }
        e.setIsCleared(history.isCleared());
        e.setScore(history.getScore());
        e.setCurrentQuestionIndex(history.getCurrentQuestionIndex());
        return e;
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
