package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.StartRecordV2Data;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public class StartRecordV2Dto {

    @Schema(name = "StartRecordV2DtoRequest")
    public record Request(
        RecordSessionTypeV2 sessionType,
        Long subjectId,
        Long taskId,
        MonitoredApp appId
    ) {
        public StartRecordV2Data.Command toCommand(Actor actor) {
            return new StartRecordV2Data.Command(
                sessionType,
                subjectId,
                taskId,
                appId,
                actor
            );
        }
    }

    @Schema(name = "StartRecordV2DtoResponse")
    public record Response(
        Instant startedAt,
        RecordSessionV2Dto.Session session
    ) {
        public static Response from(StartRecordV2Data.Result result) {
            return new Response(
                result.startedAt(),
                RecordSessionV2Dto.Session.from(result.session())
            );
        }
    }
}
