package com.process.clash.adapter.persistence.recordsessionsegment;

import com.process.clash.adapter.persistence.recordsession.RecordSessionJpaEntity;
import com.process.clash.domain.record.entity.RecordSessionSegment;
import org.springframework.stereotype.Component;

@Component
public class RecordSessionSegmentJpaMapper {

    public RecordSessionSegmentJpaEntity toJpaEntity(
        RecordSessionSegment segment,
        RecordSessionJpaEntity session
    ) {
        return RecordSessionSegmentJpaEntity.create(
            session,
            segment.appId(),
            segment.startedAt()
        );
    }

    public RecordSessionSegment toDomain(RecordSessionSegmentJpaEntity entity) {
        return new RecordSessionSegment(
            entity.getId(),
            entity.getSession().getId(),
            entity.getAppId(),
            entity.getStartedAt(),
            entity.getEndedAt()
        );
    }
}
