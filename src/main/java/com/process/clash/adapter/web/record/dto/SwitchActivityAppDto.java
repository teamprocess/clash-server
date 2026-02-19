package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.SwitchActivityAppData;
import com.process.clash.domain.record.enums.MonitoredApp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class SwitchActivityAppDto {

    @Schema(name = "SwitchActivityAppDtoRequest")
    public record Request(
        @NotNull(message = "앱 ID는 필수입니다.")
        MonitoredApp appId
    ) {}

    @Schema(name = "SwitchActivityAppDtoResponse")
    public record Response(
        Instant switchedAt,
        RecordSessionDto.Session session
    ) {
        public static Response from(SwitchActivityAppData.Result result) {
            return new Response(
                result.switchedAt(),
                RecordSessionDto.Session.from(result.session())
            );
        }
    }
}
