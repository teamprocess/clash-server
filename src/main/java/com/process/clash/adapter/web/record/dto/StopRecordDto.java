package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.StopRecordData;
import java.time.Instant;
import java.time.ZoneOffset;

public class StopRecordDto {

    public record Response(
            Long taskId,
            Instant stoppedAt
    ) {
        public static Response from(StopRecordData.Result result) {
            return new Response(
                    result.taskId(),
                    result.stoppedAt().atZone(ZoneOffset.UTC).toInstant()
            );
        }
    }
}
