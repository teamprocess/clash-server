package com.process.clash.adapter.persistence.record.v2.session;

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

        return new RecordSessionV2(
            entity.getId(),
            entity.getUser().getId(),
            sessionType,
            entity.getTaskSession() == null ? null : entity.getTaskSession().getSubject().getId(),
            entity.getTaskSession() == null ? null : entity.getTaskSession().getSubject().getName(),
            entity.getTaskSession() == null || entity.getTaskSession().getTask() == null
                ? null
                : entity.getTaskSession().getTask().getId(),
            entity.getTaskSession() == null || entity.getTaskSession().getTask() == null
                ? null
                : entity.getTaskSession().getTask().getName(),
            null,
            entity.getStartedAt(),
            entity.getEndedAt()
        );
    }
}
