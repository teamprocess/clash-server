package com.process.clash.adapter.persistence.record.v2.task;

import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import org.springframework.stereotype.Component;

@Component
public class RecordTaskV2JpaMapper {

    public RecordTaskV2 toDomain(RecordTaskV2JpaEntity entity) {
        return new RecordTaskV2(
            entity.getId(),
            entity.getSubject().getId(),
            entity.getName(),
            0L,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
