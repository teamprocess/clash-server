package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.RecordSessionV2Data;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class RecordSessionV2Mapper {

    private RecordSessionV2Mapper() {
    }

    public static RecordSessionV2Data.Session toSession(RecordSessionV2 session, ZoneId unusedRecordZoneId) {
        return toSession(session, session.startedAt(), session.endedAt());
    }

    public static RecordSessionV2Data.Session toSession(
        RecordSessionV2 session,
        ZoneId recordZoneId,
        LocalDateTime startedAt,
        LocalDateTime endedAt
    ) {
        Instant startedAtInstant = startedAt.atZone(recordZoneId).toInstant();
        Instant endedAtInstant = endedAt == null ? null : endedAt.atZone(recordZoneId).toInstant();
        return toSession(session, startedAtInstant, endedAtInstant);
    }

    public static RecordSessionV2Data.Session toSession(
        RecordSessionV2 session,
        Instant startedAt,
        Instant endedAt
    ) {
        if (session.sessionType() == RecordSessionTypeV2.DEVELOP) {
            return RecordSessionV2Data.Session.develop(
                session.id(),
                startedAt,
                endedAt,
                session.appId()
            );
        }

        return RecordSessionV2Data.Session.task(
            session.id(),
            startedAt,
            endedAt,
            session.subjectId(),
            session.subjectName(),
            session.taskId(),
            session.taskName()
        );
    }
}
