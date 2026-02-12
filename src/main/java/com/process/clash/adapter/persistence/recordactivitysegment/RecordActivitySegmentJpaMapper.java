package com.process.clash.adapter.persistence.recordactivitysegment;

import com.process.clash.adapter.persistence.studysession.StudySessionJpaEntity;
import com.process.clash.domain.record.entity.RecordActivitySegment;
import org.springframework.stereotype.Component;

@Component
public class RecordActivitySegmentJpaMapper {

    public RecordActivitySegmentJpaEntity toJpaEntity(
        RecordActivitySegment segment,
        StudySessionJpaEntity session
    ) {
        return RecordActivitySegmentJpaEntity.create(
            session,
            segment.appName(),
            segment.startedAt()
        );
    }

    public RecordActivitySegment toDomain(RecordActivitySegmentJpaEntity entity) {
        return new RecordActivitySegment(
            entity.getId(),
            entity.getSession().getId(),
            entity.getAppName(),
            entity.getStartedAt(),
            entity.getEndedAt()
        );
    }
}
