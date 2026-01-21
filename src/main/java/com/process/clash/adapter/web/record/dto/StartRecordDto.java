package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.StartRecordData;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

public class StartRecordDto {

    @Schema(name = "StartRecordDtoRequest")

    public record Request(
            @NotNull(message = "taskId는 필수 입력값입니다.")
            Long taskId
    ) {}

    @Schema(name = "StartRecordDtoResponse")

    public record Response(
       Instant startedTime
    ) {

        public static Response from(StartRecordData.Result result) {
            return new Response(
                    result.startedAt()
            );
        }
    }
}
