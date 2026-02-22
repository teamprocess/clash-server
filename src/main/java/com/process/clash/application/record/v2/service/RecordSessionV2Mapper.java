package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.RecordSessionV2Data;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;

public final class RecordSessionV2Mapper {

    private RecordSessionV2Mapper() {
    }

    public static RecordSessionV2Data.Session toSession(RecordSessionV2 session) {
        return toSession(session, session.startedAt(), session.endedAt());
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
