package com.process.clash.domain.record.v2.entity;

import java.time.Instant;

public record RecordTaskV2(
    Long id,
    Long subjectId,
    String name,
    Long studyTime,
    Instant createdAt,
    Instant updatedAt
) {

    public static RecordTaskV2 create(String name, Long subjectId) {
        Instant now = Instant.now();
        return new RecordTaskV2(
            null,
            subjectId,
            name,
            0L,
            now,
            now
        );
    }

    public RecordTaskV2 changeName(String name) {
        return new RecordTaskV2(
            this.id,
            this.subjectId,
            name,
            this.studyTime,
            this.createdAt,
            Instant.now()
        );
    }

    public RecordTaskV2 changeStudyTime(Long studyTime) {
        return new RecordTaskV2(
            this.id,
            this.subjectId,
            this.name,
            studyTime,
            this.createdAt,
            this.updatedAt
        );
    }
}
