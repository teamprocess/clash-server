package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.task.TaskJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.enums.RecordType;
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
            studySession.recordType(),
            studySession.appName(),
            studySession.startedAt()
        );
    }

    public StudySession toDomain(StudySessionJpaEntity studySessionJpaEntity) {
        RecordType recordType = studySessionJpaEntity.getRecordType();
        if (recordType == null) {
            recordType = studySessionJpaEntity.getTask() == null ? RecordType.ACTIVITY : RecordType.TASK;
        }

        return StudySession.create(
            studySessionJpaEntity.getId(),
            userJpaMapper.toDomain(studySessionJpaEntity.getUser()),
            studySessionJpaEntity.getTask() == null
                ? null
                : taskJpaMapper.toDomain(studySessionJpaEntity.getTask()),
            recordType,
            studySessionJpaEntity.getAppName(),
            studySessionJpaEntity.getStartedAt(),
            studySessionJpaEntity.getEndedAt()
        );
    }
}
