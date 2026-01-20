package com.process.clash.adapter.persistence.roadmap.missionhistory;

import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMissionHistoryJpaMapper {

    public UserMissionHistoryJpaEntity toJpaEntity(UserMissionHistory history, UserJpaEntity userEntity, MissionJpaEntity missionEntity) {
        return new UserMissionHistoryJpaEntity(
                history.getId(),
                userEntity,
                missionEntity,
                history.isCleared(),
                history.getCorrectCount(),
                history.getTotalCount(),
                history.getCurrentQuestionIndex(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );
    }

    public UserMissionHistory toDomain(UserMissionHistoryJpaEntity entity) {
        return new UserMissionHistory(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getMission() != null ? entity.getMission().getId() : null,
                entity.isCleared(),
                entity.getCorrectCount(),
                entity.getTotalCount(),
                entity.getCurrentQuestionIndex()
        );
    }
}
