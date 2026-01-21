package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.StopRecordData;
import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

public class StopRecordDto {

    @Schema(name = "StopRecordDtoResponse")

    public record Response(
            Long taskId,
            Instant stoppedAt
    ) {
        public static Response from(StopRecordData.Result result) {
            return new Response(
                    result.taskId(),
                    result.stoppedAt()
            );
        }
    }
}
