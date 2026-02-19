package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

public class StartRecordDto {

    @Schema(name = "StartRecordDtoRequest")

    public record Request(
            RecordType recordType,
            Long taskId,
            MonitoredApp appId
    ) {}

    @Schema(name = "StartRecordDtoResponse")

    public record Response(
       Instant startedTime,
       RecordSessionDto.Session session
    ) {

        public static Response from(StartRecordData.Result result) {
            return new Response(
                    result.startedAt(),
                    RecordSessionDto.Session.from(result.session())
            );
        }
    }
}
