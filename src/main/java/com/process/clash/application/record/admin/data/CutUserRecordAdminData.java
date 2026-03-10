package com.process.clash.application.record.admin.data;

import com.process.clash.application.record.v2.data.RecordSessionV2Data;
import java.time.Instant;

public class CutUserRecordAdminData {

    public record Command(
        Long userId
    ) {
    }

    public record Result(
        Instant endedAt,
        RecordSessionV2Data.Session session
    ) {
        public static Result from(Instant endedAt, RecordSessionV2Data.Session session) {
            return new Result(endedAt, session);
        }
    }
}