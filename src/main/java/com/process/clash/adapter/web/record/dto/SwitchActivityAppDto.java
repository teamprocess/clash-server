package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.SwitchActivityAppData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class SwitchActivityAppDto {

    @Schema(name = "SwitchActivityAppDtoRequest")
    public record Request(
        @NotBlank(message = "앱 이름은 필수입니다.")
        String appName
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
