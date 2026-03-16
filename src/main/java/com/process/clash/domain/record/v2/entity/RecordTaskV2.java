package com.process.clash.domain.record.v2.entity;

import java.time.Instant;
import java.time.LocalDate;

public record RecordTaskV2(
    Long id,
    Long userId,
    Long subjectId,
    String name,
    boolean completed,
    Long studyTime,
    LocalDate recordDate,
    Instant createdAt,
    Instant updatedAt
) {

    public static RecordTaskV2 create(String name, Long userId, Long subjectId, LocalDate recordDate) {
        Instant now = Instant.now();
        return new RecordTaskV2(
            null,
            userId,
            subjectId,
            name,
            false,
            0L,
            recordDate,
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
            this.recordDate,
            this.createdAt,
            Instant.now()
        );
    }

    public RecordTaskV2 changeDetails(String name, Long subjectId) {
        return new RecordTaskV2(
            this.id,
            this.userId,
            subjectId,
            name,
            this.completed,
            this.studyTime,
            this.recordDate,
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
            this.recordDate,
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
            this.recordDate,
            this.createdAt,
            this.updatedAt
        );
    }

    public boolean belongsToRecordDate(LocalDate date) {
        return this.recordDate.equals(date);
    }
}
