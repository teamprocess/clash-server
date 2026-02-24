package com.process.clash.adapter.persistence.record.v2.segment;

import com.process.clash.adapter.persistence.record.v2.session.RecordDevelopSessionV2JpaEntity;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import org.springframework.stereotype.Component;

@Component
public class RecordDevelopSessionSegmentV2JpaMapper {

    public RecordDevelopSessionSegmentV2JpaEntity toJpaEntity(
        RecordDevelopSessionSegmentV2 segment,
        RecordDevelopSessionV2JpaEntity developSession
    ) {
        return RecordDevelopSessionSegmentV2JpaEntity.create(
            developSession,
            segment.appId(),
            segment.startedAt(),
            segment.endedAt()
        );
    }

    public RecordDevelopSessionSegmentV2 toDomain(RecordDevelopSessionSegmentV2JpaEntity entity) {
        return new RecordDevelopSessionSegmentV2(
            entity.getId(),
            entity.getDevelopSession().getId(),
            entity.getAppId(),
            entity.getStartedAt(),
            entity.getEndedAt()
        );
    }
}
