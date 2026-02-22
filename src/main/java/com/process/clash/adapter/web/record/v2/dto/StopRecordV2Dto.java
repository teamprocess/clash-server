package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.StopRecordV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public class StopRecordV2Dto {

    @Schema(name = "StopRecordV2DtoResponse")
    public record Response(
        Instant stoppedAt,
        RecordSessionV2Dto.Session session
    ) {
        public static Response from(StopRecordV2Data.Result result) {
            return new Response(
                result.stoppedAt(),
                RecordSessionV2Dto.Session.from(result.session())
            );
        }
    }
}
