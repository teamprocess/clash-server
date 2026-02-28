package com.process.clash.domain.record.v2.entity;

import java.time.Instant;

public record RecordTaskV2(
    Long id,
    Long userId,
    Long subjectId,
    String name,
    boolean completed,
    Long studyTime,
    Instant createdAt,
    Instant updatedAt
) {

    public static RecordTaskV2 create(String name, Long userId, Long subjectId) {
        Instant now = Instant.now();
        return new RecordTaskV2(
            null,
            userId,
            subjectId,
            name,
            false,
            0L,
            now,
            now
        );
    }

    public RecordTaskV2 changeName(String name) {
        return new RecordTaskV2(
            this.id,
            this.userId,
            this.subjectId,
            name,
            this.completed,
            this.studyTime,
            this.createdAt,
            Instant.now()
        );
    }

    public RecordTaskV2 changeCompleted(boolean completed) {
        return new RecordTaskV2(
            this.id,
            this.userId,
            this.subjectId,
            this.name,
            completed,
            this.studyTime,
            this.createdAt,
            Instant.now()
        );
    }

    public RecordTaskV2 changeStudyTime(Long studyTime) {
        return new RecordTaskV2(
            this.id,
            this.userId,
            this.subjectId,
            this.name,
            this.completed,
            studyTime,
            this.createdAt,
            this.updatedAt
        );
    }
}
