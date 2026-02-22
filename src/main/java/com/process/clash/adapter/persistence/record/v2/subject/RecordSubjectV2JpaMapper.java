package com.process.clash.adapter.persistence.record.v2.subject;

import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import org.springframework.stereotype.Component;

@Component
public class RecordSubjectV2JpaMapper {

    public RecordSubjectV2 toDomain(RecordSubjectV2JpaEntity entity) {
        return new RecordSubjectV2(
            entity.getId(),
            entity.getUser().getId(),
            entity.getName(),
            0L,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
