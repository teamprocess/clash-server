package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import org.springframework.stereotype.Component;

@Component
public class RecordSessionV2JpaMapper {

    public RecordActiveSessionV2JpaEntity toActiveEntity(RecordSessionV2 session, UserJpaEntity user) {
        return RecordActiveSessionV2JpaEntity.create(
            user,
            session.sessionType(),
            session.startedAt()
        );
    }

    public RecordSessionV2 toDomain(RecordActiveSessionV2JpaEntity entity) {
        RecordSessionTypeV2 sessionType = entity.getSessionType();

        if (sessionType == RecordSessionTypeV2.DEVELOP) {
            return new RecordSessionV2(
                entity.getId(),
                entity.getUser().getId(),
                sessionType,
                null,
                null,
                null,
                null,
                entity.getDevelopSession() == null ? null : entity.getDevelopSession().getAppId(),
                entity.getStartedAt(),
                entity.getEndedAt()
            );
        }

        RecordTaskSessionV2JpaEntity taskSession = entity.getTaskSession();
        RecordTaskV2JpaEntity task = taskSession == null ? null : taskSession.getTask();

        return new RecordSessionV2(
            entity.getId(),
            entity.getUser().getId(),
            sessionType,
            taskSession == null ? null : taskSession.getSubject().getId(),
            taskSession == null ? null : taskSession.getSubject().getName(),
            task == null ? null : task.getId(),
            task == null ? null : task.getName(),
            null,
            entity.getStartedAt(),
            entity.getEndedAt()
        );
    }
}
