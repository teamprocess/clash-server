package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.task.TaskJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.entity.StudySession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudySessionJpaMapper {

    private final TaskJpaMapper taskJpaMapper;
    private final UserJpaMapper userJpaMapper;

    public StudySessionJpaEntity toJpaEntity(StudySession studySession, UserJpaEntity user, TaskJpaEntity task) {
        return StudySessionJpaEntity.create(
            user,
            task,
            studySession.startedAt()
        );
    }

    public StudySession toDomain(StudySessionJpaEntity studySessionJpaEntity) {
        return StudySession.create(
            studySessionJpaEntity.getId(),
            userJpaMapper.toDomain(studySessionJpaEntity.getUser()),
            taskJpaMapper.toDomain(studySessionJpaEntity.getTask()),
            studySessionJpaEntity.getStartedAt(),
            studySessionJpaEntity.getEndedAt()
        );
    }
}
