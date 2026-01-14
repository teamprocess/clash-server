package com.process.clash.adapter.persistence.session;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.task.TaskJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.model.entity.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionJpaMapper {

    public SessionJpaEntity toJpaEntity(Session session, UserJpaEntity user, TaskJpaEntity task) {
        return SessionJpaEntity.create(
            user,
            task,
            session.startedAt()
        );
    }

    public Session toDomain(SessionJpaEntity sessionJpaEntity) {
        return Session.create(
            sessionJpaEntity.getId(),
            UserJpaMapper.toDomain(sessionJpaEntity.getUser()),
            TaskJpaMapper.toDomain(sessionJpaEntity.getTask()),
            sessionJpaEntity.getStartedAt(),
            sessionJpaEntity.getEndedAt()
        );
    }
}