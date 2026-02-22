package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.SwitchDevelopAppV2Data;
import com.process.clash.domain.record.enums.MonitoredApp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class SwitchDevelopAppV2Dto {

    @Schema(name = "SwitchDevelopAppV2DtoRequest")
    public record Request(
        @NotNull(message = "앱 ID는 필수입니다.")
        MonitoredApp appId
    ) {
        public SwitchDevelopAppV2Data.Command toCommand(Actor actor) {
            return new SwitchDevelopAppV2Data.Command(actor, appId);
        }
    }

    @Schema(name = "SwitchDevelopAppV2DtoResponse")
    public record Response(
        Instant switchedAt,
        RecordSessionV2Dto.Session session
    ) {
        public static Response from(SwitchDevelopAppV2Data.Result result) {
            return new Response(
                result.switchedAt(),
                RecordSessionV2Dto.Session.from(result.session())
            );
        }
    }
}
