package com.process.clash.application.record.service;

import com.process.clash.application.record.data.RecordSessionData;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.enums.RecordType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class RecordSessionMapper {

    private RecordSessionMapper() {
    }

    public static RecordSessionData.Session toSession(StudySession session, ZoneId unusedRecordZoneId) {
        return toSession(session, session.startedAt(), session.endedAt());
    }

    public static RecordSessionData.Session toSession(
        StudySession session,
        ZoneId recordZoneId,
        LocalDateTime startedAt,
        LocalDateTime endedAt
    ) {
        Instant startedAtInstant = startedAt.atZone(recordZoneId).toInstant();
        Instant endedAtInstant = endedAt == null ? null : endedAt.atZone(recordZoneId).toInstant();
        return toSession(session, startedAtInstant, endedAtInstant);
    }

    public static RecordSessionData.Session toSession(
        StudySession session,
        Instant startedAt,
        Instant endedAt
    ) {
        if (session.recordType() == RecordType.ACTIVITY || session.task() == null) {
            return RecordSessionData.Session.activity(
                session.id(),
                startedAt,
                endedAt,
                session.appName()
            );
        }

        return RecordSessionData.Session.task(
            session.id(),
            startedAt,
            endedAt,
            session.task().id(),
            session.task().name()
        );
    }
}
