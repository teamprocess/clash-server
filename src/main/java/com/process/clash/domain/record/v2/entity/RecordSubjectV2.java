package com.process.clash.domain.record.v2.entity;

import java.time.Instant;

public record RecordSubjectV2(
    Long id,
    Long userId,
    String name,
    Long studyTime,
    Instant createdAt,
    Instant updatedAt
) {

    public static RecordSubjectV2 create(String name, Long userId) {
        Instant now = Instant.now();
        return new RecordSubjectV2(
            null,
            userId,
            name,
            0L,
            now,
            now
        );
    }

    public RecordSubjectV2 changeName(String name) {
        return new RecordSubjectV2(
            this.id,
            this.userId,
            name,
            this.studyTime,
            this.createdAt,
            Instant.now()
        );
    }

    public RecordSubjectV2 changeStudyTime(Long studyTime) {
        return new RecordSubjectV2(
            this.id,
            this.userId,
            this.name,
            studyTime,
            this.createdAt,
            this.updatedAt
        );
    }
}
